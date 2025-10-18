package com.vegas.sistema_gestion_operativa.unit.branches.controller;

import com.vegas.sistema_gestion_operativa.branches.infrastructure.controller.BranchController;
import com.vegas.sistema_gestion_operativa.branches.application.service.BranchService;
import com.vegas.sistema_gestion_operativa.unit.branches.factory.FakeBranchFactory;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BranchControllerTest {

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    private FakeBranchFactory fakeBranchFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fakeBranchFactory = new FakeBranchFactory(new net.datafaker.Faker());
    }

    // TODO: Fix Create Test
    /*@Test
    void testCreate() {
        CreateBranchDto dto = new CreateBranchDto("Sucursal 1", "Dirección 1", "123456");
        Branch branch = fakeBranchFactory.createBranch();
        when(branchService.create(dto)).thenReturn(branch);

        ResponseEntity<Branch> response = branchController.create(dto);

        assertEquals(branch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }*/

    /*@Test
    void testFindAll() {
        Branch branch1 = fakeBranchFactory.createBranch();
        Branch branch2 = fakeBranchFactory.createBranch();
        List<Branch> branches = Arrays.asList(branch1, branch2);
        when(branchService.findAll()).thenReturn(branches);

        ResponseEntity<List<Branch>> response = branchController.findAll();

        assertEquals(branches, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testFindById() {
        Branch branch = fakeBranchFactory.createBranch();
        when(branchService.findById("1")).thenReturn(branch);

        ResponseEntity<Branch> response = branchController.findById("1");

        assertEquals(branch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdate() {
        UpdateBranchDto dto = new UpdateBranchDto("1", "Sucursal 1", "Dirección 1", "123456");
        Branch updatedBranch = fakeBranchFactory.createBranch();
        when(branchService.update(ArgumentMatchers.any(UpdateBranchDto.class))).thenReturn(updatedBranch);

        ResponseEntity<Branch> response = branchController.update("1", dto);

        assertEquals(updatedBranch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        Branch deletedBranch = fakeBranchFactory.createBranch();

        when(branchService.delete("1")).thenReturn(deletedBranch);
        ResponseEntity<Branch> response = branchController.delete("1");

        assertEquals(deletedBranch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(branchService, times(1)).delete("1");
    }*/
}