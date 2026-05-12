package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        new File("output").mkdirs();

        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                writeRecord(out,
                        scanner.nextInt(),
                        Double.parseDouble(scanner.next()),
                        scanner.next(),
                        TipTranzactie.valueOf(scanner.next()),
                        Status.PENDING);
            }
        }

        try (RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw")) {
            while (scanner.hasNext()) {
                String command = scanner.next();
                switch (command) {
                    case "READ":
                        int readIdx = scanner.nextInt();
                        System.out.println(readRecord(raf, readIdx));
                        break;
                    case "UPDATE":
                        int updateIdx = scanner.nextInt();
                        Status status = Status.valueOf(scanner.next());
                        raf.seek((long) updateIdx * RECORD_SIZE + 23);
                        raf.write(status.ordinal());
                        System.out.println("Updated [" + updateIdx + "]: " + status);
                        break;
                    case "PRINT_ALL":
                        for (int i = 0; i < n; i++) {
                            System.out.println(readRecord(raf, i));
                        }
                        break;
                    default:
                        throw new IllegalArgumentException("Comandă necunoscută: " + command);
                }
            }
        }
    }

    private static void writeRecord(DataOutputStream out, int id, double suma, String data,
                                    TipTranzactie tip, Status status) throws IOException {
        byte[] record = new byte[RECORD_SIZE];
        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(id);
        buffer.putDouble(suma);
        byte[] dataBytes = data.getBytes();
        System.arraycopy(dataBytes, 0, record, 12, Math.min(dataBytes.length, 10));
        for (int i = 12 + dataBytes.length; i < 22; i++) {
            record[i] = ' ';
        }
        record[22] = (byte) tip.ordinal();
        record[23] = (byte) status.ordinal();
        out.write(record);
    }

    private static String readRecord(RandomAccessFile raf, int idx) throws IOException {
        byte[] record = new byte[RECORD_SIZE];
        raf.seek((long) idx * RECORD_SIZE);
        raf.readFully(record);

        ByteBuffer buffer = ByteBuffer.wrap(record).order(ByteOrder.LITTLE_ENDIAN);
        int id = buffer.getInt();
        double suma = buffer.getDouble();
        String data = new String(record, 12, 10).trim();
        TipTranzactie tip = TipTranzactie.values()[record[22]];
        Status status = Status.values()[record[23]];
        return String.format(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s",
                idx, id, data, tip, suma, status);
    }

    private enum Status {
        PENDING,
        PROCESSED,
        REJECTED
    }
}
