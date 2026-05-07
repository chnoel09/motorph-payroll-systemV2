/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.service;

import java.time.LocalDate;
import java.util.List;

import com.mycompany.oop.model.Leave;
import com.mycompany.oop.repository.LeaveRepository;
import com.mycompany.oop.repository.CsvLeaveRepository;

public class LeaveService {

    private LeaveRepository repository;

    public LeaveService() {
        this.repository = new CsvLeaveRepository();
    }

    // ================= FILE LEAVE =================
    
    public Leave fileLeave(int employeeId,
                           String type,
                           String start,
                           String end,
                           String reason) {
                
        validateLeaveInput(type, start, end, reason);

        int newId = repository.getAllLeaves().size() + 1;

        Leave leave = new Leave(
                newId,
                employeeId,
                type,
                start,
                end,
                reason,
                "PENDING"
        );

        repository.addLeave(leave);

        return leave;
        
    }   

    // ================= VALIDATION =================
    private void validateLeaveInput(String type,
                                    String start,
                                    String end,
                                    String reason) {
                                    
        if (type == null || type.isEmpty()
                || start == null || start.isEmpty()
                || end == null || end.isEmpty()
                || reason == null || reason.isEmpty()) {

            throw new IllegalArgumentException(
                    "All fields are required.");
        }

        try {

            LocalDate startDate = LocalDate.parse(start);
            LocalDate endDate = LocalDate.parse(end);

            if (endDate.isBefore(startDate)) {
                throw new IllegalArgumentException(
                        "End date cannot be before start date.");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Invalid date format. Use YYYY-MM-DD.");
        }
    }

    // ================= APPROVAL =================
    public void approveLeave(int leaveId) {

        Leave leave = repository.findLeave(leaveId);

        if (leave == null) {
            throw new IllegalArgumentException(
                    "Leave not found.");
        }

        if (!leave.getStatus().equalsIgnoreCase("PENDING")) {
            throw new IllegalStateException(
                    "Only pending leaves can be approved.");
        }

        leave.setStatus("APPROVED");
        repository.updateLeave(leave);
    }

    // ================= REJECTION =================
    public void rejectLeave(int leaveId) {

        Leave leave = repository.findLeave(leaveId);

        if (leave == null) {
            throw new IllegalArgumentException(
                    "Leave not found.");
        }

        if (!leave.getStatus().equalsIgnoreCase("PENDING")) {
            throw new IllegalStateException(
                    "Only pending leaves can be rejected.");
        }

        leave.setStatus("REJECTED");
        repository.updateLeave(leave);
    }

    // ================= FETCH METHODS =================
    public List<Leave> getAllLeaves() {
        return repository.getAllLeaves();
    }

    public List<Leave> getLeavesByEmployee(int employeeId) {
        return repository.getLeavesByEmployee(employeeId);
    }
}













