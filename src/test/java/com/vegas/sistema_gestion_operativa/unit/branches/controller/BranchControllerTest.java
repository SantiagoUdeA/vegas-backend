package com.vegas.sistema_gestion_operativa.unit.branches.controller;

import com.vegas.sistema_gestion_operativa.branches.controller.BranchController;
import com.vegas.sistema_gestion_operativa.branches.domain.Branch;
import com.vegas.sistema_gestion_operativa.branches.dto.CreateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.dto.UpdateBranchDto;
import com.vegas.sistema_gestion_operativa.branches.service.BranchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BranchControllerTest {

    @Mock
    private BranchService branchService;

    @InjectMocks
    private BranchController branchController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        CreateBranchDto dto = new CreateBranchDto("Sucursal 1", "Dirección 1", "123456");
        Branch branch = new Branch("1", "Sucursal 1", "Dirección 1", "123456");
        when(branchService.create(dto)).thenReturn(branch);

        ResponseEntity<Branch> response = branchController.create(dto);

        assertEquals(branch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testFindAll() {
        Branch branch1 = new Branch("1", "Sucursal 1", "Dirección 1", "123456");
        Branch branch2 = new Branch("2", "Sucursal 2", "Dirección 2", "654321");
        List<Branch> branches = Arrays.asList(branch1, branch2);
        when(branchService.findAll()).thenReturn(branches);

        ResponseEntity<List<Branch>> response = branchController.findAll();

        assertEquals(branches, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testFindById() {
        Branch branch = new Branch("1", "Sucursal 1", "Dirección 1", "123456");
        when(branchService.findById("1")).thenReturn(branch);

        ResponseEntity<Branch> response = branchController.findById("1");

        assertEquals(branch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdate() {
        UpdateBranchDto dto = new UpdateBranchDto("1", "Sucursal 1", "Dirección 1", "123456");
        Branch updatedBranch = new Branch("1", "Sucursal 1", "Dirección 1", "123456");
        when(branchService.update(ArgumentMatchers.any(UpdateBranchDto.class))).thenReturn(updatedBranch);

        ResponseEntity<Branch> response = branchController.update("1", dto);

        assertEquals(updatedBranch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDelete() {
        Branch deletedBranch = new Branch("1", "Sucursal 1", "Dirección 1", "123456");

        when(branchService.delete("1")).thenReturn(deletedBranch);
        ResponseEntity<Branch> response = branchController.delete("1");

        assertEquals(deletedBranch, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(branchService, times(1)).delete("1");
    }
}