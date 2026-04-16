package com.vegas.sistema_gestion_operativa.branches.application.service;

import com.vegas.sistema_gestion_operativa.branches.IBranchApi;
import com.vegas.sistema_gestion_operativa.branches.application.dto.AssignBranchOwnerDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.BranchOwnerResponseDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.BranchResponseDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.application.factory.BranchFactory;
import com.vegas.sistema_gestion_operativa.branches.application.mapper.IBranchMapper;
import com.vegas.sistema_gestion_operativa.branches.domain.entity.Branch;
import com.vegas.sistema_gestion_operativa.branches.domain.entity.UserBranchId;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchDuplicateInformationException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchMustHaveAtLeastOneOwnerException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNameAlreadyExistsException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchNotFoundException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.BranchOwnerAlreadyAssignedException;
import com.vegas.sistema_gestion_operativa.branches.domain.exception.UserNotOwnerRoleException;
import com.vegas.sistema_gestion_operativa.branches.infrastructure.repository.IBranchRepository;
import com.vegas.sistema_gestion_operativa.branches.infrastructure.repository.IUserBranchRepository;
import com.vegas.sistema_gestion_operativa.common.context.FranchiseContext;
import com.vegas.sistema_gestion_operativa.common.exceptions.AccessDeniedException;
import com.vegas.sistema_gestion_operativa.franchise.IFranchiseApi;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseAccessDeniedException;
import com.vegas.sistema_gestion_operativa.franchise.domain.exception.FranchiseNotFoundException;
import com.vegas.sistema_gestion_operativa.users.IUserApi;
import com.vegas.sistema_gestion_operativa.users.domain.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BranchService implements IBranchApi {

    private final IBranchRepository branchRepository;
    private final IUserBranchRepository userBranchRepository;
    private final IBranchMapper branchMapper;
    private final IFranchiseApi franchiseApi;
    private final IUserApi userApi;

    @Autowired
    public BranchService(
            IBranchRepository branchRepository,
            IUserBranchRepository userBranchRepository,
            IBranchMapper branchMapper,
            IFranchiseApi franchiseApi,
            IUserApi userApi) {
        this.branchRepository = branchRepository;
        this.userBranchRepository = userBranchRepository;
        this.branchMapper = branchMapper;
        this.franchiseApi = franchiseApi;
        this.userApi = userApi;
    }

    @Transactional
    public Branch create(CreateBranchDto dto, String ownerId)
            throws BranchNameAlreadyExistsException, FranchiseNotFoundException, FranchiseAccessDeniedException {
        // Si no viene franchiseId, obtenerla del contexto (para OWNERs sin suscripción)
        Long franchiseId = dto.franchiseId();
        if (franchiseId == null) {
            franchiseId = FranchiseContext.getCurrentFranchiseId();
        }

        // Validar que la franquicia existe
        franchiseApi.assertFranchiseExists(franchiseId);

        // Validar que el usuario es dueño de la franquicia
        franchiseApi.assertUserOwnsFranchise(ownerId, franchiseId);

        if (this.branchRepository.existsByNameAndUserBranches_Id_UserId(dto.name(), ownerId))
            throw new BranchNameAlreadyExistsException("Ya existe una sede con el nombre: " + dto.name());

        if (dto.address() != null && !dto.address().isBlank() &&
            this.branchRepository.existsByFranchiseIdAndAddressIgnoreCase(franchiseId, dto.address())) {
            throw new BranchDuplicateInformationException("La dirección ya está registrada en otra sede de esta franquicia.");
        }

        if (dto.phoneNumber() != null && !dto.phoneNumber().isBlank() &&
            this.branchRepository.existsByFranchiseIdAndPhoneNumber(franchiseId, dto.phoneNumber())) {
            throw new BranchDuplicateInformationException("El teléfono ya está registrado en otra sede de esta franquicia.");
        }

        var branch = BranchFactory.createBranch(dto, franchiseId);
        branch.assignFounder(ownerId);
        return branchRepository.save(branch);
    }

    public Page<BranchResponseDto> findOwnerBranches(String userId, Pageable pageable) {
        Page<Branch> branches = this.branchRepository.findByUserBranches_Id_UserIdAndUserBranches_FounderTrue(userId, pageable);
        return branches.map(this::toBranchResponseDto);
    }

    public Branch update(String ownerId, Long branchId, UpdateBranchDto dto) throws BranchNotFoundException, AccessDeniedException {
        // TODO Evitar que se carguen datos infinitos anidados en branch (debuggear para ver)
        var branch = this.retrieveBranch(branchId);

        if (!branch.isFoundedByUser(ownerId))
            throw new AccessDeniedException("No puedes editar por que no eres el fundador de esta sucursal");

        var updatedBranch = this.branchMapper.partialUpdate(dto, branch);
        updatedBranch.setId(branchId); // TODO Arreglar esto en todos los servicios
        return this.branchRepository.save(updatedBranch);
    }

    public void assignUserToBranch(String userId, Long branchId, boolean isFounder) {
        var branch = this.retrieveBranch(branchId);
        if (isFounder) branch.assignFounder(userId);
        else branch.assignUser(userId);
        branchRepository.save(branch);
    }

    public Branch retrieveBranch(Long id) {
        return this.branchRepository.findById(id)
                .orElseThrow(() -> new BranchNotFoundException("Branch not found with id: " + id));
    }

    @Override
    public void assertUserHasAccessToBranch(String userId, Long branchId) throws AccessDeniedException {
        var branches = branchRepository.findBranchIdsByUserId(userId);
        if (!branches.contains(branchId))
            throw new AccessDeniedException("No tienes acceso a la sucursal ");
    }

    public List<BranchResponseDto> findAllBranchesByUserId(String userIdFromToken) {
        List<Branch> branches = branchRepository.findBranchesByUserId(userIdFromToken);
        return branches.stream()
                .map(this::toBranchResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Long getFranchiseIdForBranch(Long branchId) {
        var branch = this.retrieveBranch(branchId);
        return branch.getFranchiseId();
    }

    /**
     * Returns all owners (founders and co-owners) of a branch.
     * Only accessible to users who are already owners of the branch.
     *
     * @param requesterId the ID of the user making the request
     * @param branchId    the ID of the branch
     * @return list of branch owners with their details
     * @throws AccessDeniedException if the requester is not an owner of the branch
     */
    public List<BranchOwnerResponseDto> getOwnersOfBranch(String requesterId, Long branchId) throws AccessDeniedException {
        assertUserIsBranchOwner(requesterId, branchId);
        return userBranchRepository.findAllByBranchId(branchId).stream()
                .map(ub -> new BranchOwnerResponseDto(
                        ub.getUserId(),
                        userApi.getFullNameById(ub.getUserId()),
                        ub.isFounder()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Assigns a new co-owner (non-founder) to a branch.
     * The requester must already be an owner of the branch.
     * The target user is identified by their registered email.
     * They must have the OWNER role and not already be assigned to the branch.
     *
     * @param requesterId the user performing the action
     * @param branchId    the branch to assign the new owner to
     * @param dto         contains the email of the user to assign
     * @throws UserNotFoundException               if no user exists with the given email
     * @throws AccessDeniedException               if the requester is not an owner of this branch
     * @throws UserNotOwnerRoleException            if the target user is not an OWNER
     * @throws BranchOwnerAlreadyAssignedException  if the target user is already assigned
     */
    @Transactional
    public void addOwnerToBranch(String requesterId, Long branchId, AssignBranchOwnerDto dto)
            throws AccessDeniedException, UserNotFoundException, UserNotOwnerRoleException, BranchOwnerAlreadyAssignedException {

        assertUserIsBranchOwner(requesterId, branchId);

        // Resolve email → userId
        String targetUserId = userApi.getIdByEmail(dto.email());

        // Validate that the target user has the OWNER role
        String targetRole = userApi.getRoleById(targetUserId);
        if (!"OWNER".equals(targetRole)) {
            throw new UserNotOwnerRoleException(
                    "El usuario con correo '%s' no tiene el rol Dueño".formatted(dto.email()));
        }

        // Validate that the user is not already assigned to this branch
        if (userBranchRepository.existsByBranchIdAndUserId(branchId, targetUserId)) {
            throw new BranchOwnerAlreadyAssignedException(
                    "El usuario ya es dueño de esta sede");
        }

        // Assign as co-owner (founder = false)
        var branch = retrieveBranch(branchId);
        branch.assignUser(targetUserId);
        branchRepository.save(branch);
    }

    /**
     * Removes a co-owner from a branch.
     * Founders cannot be removed via this method.
     * The branch must have at least one remaining owner after removal.
     *
     * @param requesterId  the user performing the action
     * @param branchId     the branch to remove the owner from
     * @param targetUserId the user to be removed
     * @throws AccessDeniedException                    if the requester is not an owner of the branch
     * @throws BranchMustHaveAtLeastOneOwnerException  if removing would leave the branch without owners
     */
    @Transactional
    public void removeOwnerFromBranch(String requesterId, Long branchId, String targetUserId)
            throws AccessDeniedException, BranchMustHaveAtLeastOneOwnerException {

        assertUserIsBranchOwner(requesterId, branchId);

        var userBranchId = new UserBranchId(targetUserId, branchId);
        var userBranch = userBranchRepository.findById(userBranchId)
                .orElseThrow(() -> new BranchNotFoundException(
                        "El usuario con ID '%s' no está asignado a esta sede".formatted(targetUserId)));

        // Founders cannot be removed through this endpoint
        if (userBranch.isFounder()) {
            throw new AccessDeniedException("No es posible remover al fundador de la sede");
        }

        // Ensure at least one owner remains
        long ownerCount = userBranchRepository.countByBranchId(branchId);
        if (ownerCount <= 1) {
            throw new BranchMustHaveAtLeastOneOwnerException(
                    "La sede debe tener al menos un dueño asignado");
        }

        userBranchRepository.deleteById(userBranchId);
    }

    /**
     * Asserts that the given user is an owner (any role) of the given branch.
     *
     * @param userId   the user to check
     * @param branchId the branch to check membership in
     * @throws AccessDeniedException if the user is not an owner of the branch
     */
    private void assertUserIsBranchOwner(String userId, Long branchId) throws AccessDeniedException {
        if (!userBranchRepository.existsByBranchIdAndUserId(branchId, userId)) {
            throw new AccessDeniedException(
                    "No tienes permisos para gestionar los dueños de esta sede");
        }
    }

    private BranchResponseDto toBranchResponseDto(Branch branch) {
        String franchiseName = null;
        if (branch.getFranchiseId() != null) {
            franchiseName = franchiseApi.getFranchiseName(branch.getFranchiseId());
        }
        return new BranchResponseDto(
                branch.getId(),
                branch.getName(),
                branch.getAddress(),
                branch.getPhoneNumber(),
                branch.getFranchiseId(),
                franchiseName
        );
    }
}
