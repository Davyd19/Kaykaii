package Tugas4;

import java.util.Date;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.text.SimpleDateFormat;

class Faktur {
    private static int noFakturCounter = 1; // Counter untuk nomor faktur
    protected int noFaktur; // Nomor faktur
    protected String kodeBarang;
    protected String namaBarang;
    protected double hargaBarang;
    protected int jumlahBeli;

    public Faktur(String kodeBarang, String namaBarang, double hargaBarang, int jumlahBeli) {
        this.noFaktur = noFakturCounter++; // Otomatis bertambah
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.hargaBarang = hargaBarang;
        this.jumlahBeli = jumlahBeli;
    }

    public double total() {
        return hargaBarang * jumlahBeli; // Menghitung total harga
    }
}

class Penjualan extends Faktur {
    public Penjualan(String kodeBarang, String namaBarang, double hargaBarang, int jumlahBeli) {
        super(kodeBarang, namaBarang, hargaBarang, jumlahBeli);

        // Validasi input
        if (kodeBarang == null || kodeBarang.isEmpty()) {
            throw new IllegalArgumentException("Kode barang tidak boleh kosong.");
        }
        if (namaBarang == null || namaBarang.isEmpty()) {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong.");
        }
        if (hargaBarang < 0) {
            throw new IllegalArgumentException("Harga barang harus positif.");
        }
        if (jumlahBeli <= 0) {
            throw new IllegalArgumentException("Jumlah beli harus lebih dari 0.");
        }
    }

    public void display(String namaKasir) {
        System.out.println("+----------------------------------------------------+");
        System.out.println("No Faktur: " + noFaktur);
        System.out.println("Kode Barang: " + kodeBarang);
        System.out.println("Nama Barang: " + namaBarang);
        System.out.println("Harga Barang: " + hargaBarang);
        System.out.println("Jumlah Beli: " + jumlahBeli);
        System.out.println("TOTAL: " + total());
        System.out.println("+----------------------------------------------------+");
        System.out.println("Kasir: " + namaKasir);
        System.out.println("+----------------------------------------------------+");
    }
}

public class Tugas4 {
    private static Scanner scanner = new Scanner(System.in);
    private static Random random = new Random();

    public static void main(String[] args) {
        while (!login()) {
            System.out.println("Login gagal. Silakan coba lagi.\n");
        }

        String namaKasir = "Davyd Yehuda";
        System.out.println("\nSelamat Datang di Supermarket");
        displayTanggalDanWaktu();
        System.out.println("+----------------------------------------------------+");

        while (true) {
            try {
                inputFaktur(namaKasir);
            } catch (InputMismatchException e) {
                System.out.println("Error: Pastikan harga dan jumlah beli berupa angka.");
                scanner.nextLine(); // Menghapus input yang salah
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }

            System.out.print("Apakah ingin transaksi lain? (y/n): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("n")) {
                break;
            }
        }

        System.out.println("Terima kasih telah berbelanja!");
        scanner.close();
    }

    private static boolean login() {
        String username = "admin";
        String password = "1234";
        String captcha = generateCaptcha();

        System.out.println("+----------------------------------------------------+");
        System.out.print("Username: ");
        String inputUsername = scanner.nextLine().trim(); // Menggunakan trim untuk menghapus spasi tambahan
        System.out.print("Password: ");
        String inputPassword = scanner.nextLine();
        System.out.println("Captcha: " + captcha);
        System.out.print("Masukkan Captcha: ");
        String inputCaptcha = scanner.nextLine();

        if (inputUsername.equals(username) && inputPassword.equals(password) && inputCaptcha.equalsIgnoreCase(captcha)) {
            System.out.println("Login berhasil.");
            return true;
        } else {
            return false;
        }
    }

    private static String generateCaptcha() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captcha = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            captcha.append(characters.charAt(random.nextInt(characters.length())));
        }
        return captcha.toString();
    }

    private static void displayTanggalDanWaktu() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
        System.out.println("Tanggal dan Waktu: " + formatter.format(now));
    }

    private static void inputFaktur(String namaKasir) {
        System.out.print("Masukkan Kode Barang: ");
        String kodeBarang = scanner.nextLine();
        System.out.print("Masukkan Nama Barang: ");
        String namaBarang = scanner.nextLine();
        System.out.print("Masukkan Harga Barang: ");
        double hargaBarang = scanner.nextDouble();
        System.out.print("Masukkan Jumlah Beli: ");
        int jumlahBeli = scanner.nextInt();
        scanner.nextLine(); // Menghapus newline yang tersisa

        Penjualan penjualan = new Penjualan(kodeBarang, namaBarang, hargaBarang, jumlahBeli);
        penjualan.display(namaKasir);
    }
}
