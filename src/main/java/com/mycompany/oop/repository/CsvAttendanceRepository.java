/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oop.repository;

import com.mycompany.oop.model.AttendanceRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvAttendanceRepository implements AttendanceRepository {

    private final String filePath = "src/main/resources/attendance.csv";

    @Override
    public void saveAttendance(AttendanceRecord record) {
        try {
            File file = new File(filePath);
            boolean isNewFile = !file.exists() || file.length() == 0;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
                if (isNewFile) {
                    bw.write("employeeId,date,timeIn,timeOut");
                    bw.newLine();
                } else {
                    bw.newLine();
                }

                bw.write(formatRecord(record));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAttendance(AttendanceRecord updatedRecord) {
        List<AttendanceRecord> records = findAll();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write("employeeId,date,timeIn,timeOut");
            bw.newLine();

            for (AttendanceRecord record : records) {
                if (record.getEmployeeId() == updatedRecord.getEmployeeId()
                        && record.getDate().equals(updatedRecord.getDate())
                        && record.getTimeIn().equals(updatedRecord.getTimeIn())) {
                    record = updatedRecord;
                }

                bw.write(formatRecord(record));
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<AttendanceRecord> findAll() {
        List<AttendanceRecord> records = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            return records;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] data = line.split(",", -1);

                if (data.length < 4) continue;

                AttendanceRecord record = new AttendanceRecord(
                        Integer.parseInt(data[0].trim()),
                        data[1].trim(),
                        data[2].trim(),
                        data[3].trim()
                );

                records.add(record);
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    public List<AttendanceRecord> findByEmployeeId(int employeeId) {
        List<AttendanceRecord> filtered = new ArrayList<>();

        for (AttendanceRecord record : findAll()) {
            if (record.getEmployeeId() == employeeId) {
                filtered.add(record);
            }
        }

        return filtered;
    }

    @Override
    public AttendanceRecord findByEmployeeAndDate(int employeeId, String date) {
        List<AttendanceRecord> records = findAll();

        for (int i = records.size() - 1; i >= 0; i--) {
            AttendanceRecord record = records.get(i);
            if (record.getEmployeeId() == employeeId && record.getDate().equals(date)) {
                if (record.getTimeOut() == null || record.getTimeOut().isEmpty()) {
                    return record;
                }
            }
        }

        return null;
    }

    private String formatRecord(AttendanceRecord record) {
        return record.getEmployeeId() + "," +
               record.getDate() + "," +
               record.getTimeIn() + "," +
               record.getTimeOut();
    }
}
