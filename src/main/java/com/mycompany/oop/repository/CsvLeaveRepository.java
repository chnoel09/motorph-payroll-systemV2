/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.repository;

import java.io.*;
import java.util.*;
import com.mycompany.oop.model.Leave;

public class CsvLeaveRepository implements LeaveRepository {

    private final String filePath = "src/main/resources/leaves.csv";

    @Override
    public void addLeave(Leave leave) {

        try (FileWriter fw = new FileWriter(filePath, true);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write(
                leave.getLeaveId() + "," +
                leave.getEmployeeId() + "," +
                leave.getLeaveType() + "," +
                leave.getStartDate() + "," +
                leave.getEndDate() + "," +
                leave.getReason() + "," +
                leave.getStatus()
            );
            bw.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateLeave(Leave updatedLeave) {

        List<Leave> leaves = getAllLeaves();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(filePath))) {

            for (Leave leave : leaves) {

                if (leave.getLeaveId() ==
                    updatedLeave.getLeaveId()) {

                    leave = updatedLeave;
                }

                bw.write(
                    leave.getLeaveId() + "," +
                    leave.getEmployeeId() + "," +
                    leave.getLeaveType() + "," +
                    leave.getStartDate() + "," +
                    leave.getEndDate() + "," +
                    leave.getReason() + "," +
                    leave.getStatus()
                );
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Leave findLeave(int leaveId) {

        for (Leave leave : getAllLeaves()) {
            if (leave.getLeaveId() == leaveId) {
                return leave;
            }
        }
        return null;
    }

    @Override
    public List<Leave> getAllLeaves() {

        List<Leave> leaves = new ArrayList<>();

        try (BufferedReader br =
                     new BufferedReader(
                             new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                leaves.add(new Leave(
                        Integer.parseInt(data[0]),
                        Integer.parseInt(data[1]),
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        data[6]
                ));
            }

        } catch (IOException e) {
            // file may not exist yet
        }

        return leaves;
    }

    @Override
    public List<Leave> getLeavesByEmployee(int employeeId) {

        List<Leave> result = new ArrayList<>();

        for (Leave leave : getAllLeaves()) {
            if (leave.getEmployeeId() == employeeId) {
                result.add(leave);
            }
        }

        return result;
    }
}
