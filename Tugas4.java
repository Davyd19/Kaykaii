package Tugas4;

import java.util.Scanner;
import java.util.InputMismatchException;

class Faktur {
    private static int noFakturCounter = 1; // Counter untuk no faktur
    protected int noFaktur; // Nomor faktur
    protected String kodeBarang;
    protected String namaBarang;
    protected double hargaBarang;
    protected int jumlahBeli;

    public Faktur(String kodeBarang, String namaBarang, double hargaBarang, int jumlahBeli) {
        this.noFaktur = noFakturCounter++; // Otomatis dan tidak dapat diubah
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
        super(kodeBarang, namaBarang, hargaBarang, jumlahBeli); // Memanggil constructor dari kelas Faktur

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
        if (jumlahBeli < 0) {
            throw new IllegalArgumentException("Jumlah beli harus positif.");
        }
    }

    public void display() {
        System.out.println("");
        System.out.println("No Faktur: " + noFaktur);
        System.out.println("Kode Barang: " + kodeBarang);
        System.out.println("Nama Barang: " + namaBarang);
        System.out.println("Harga Barang: " + hargaBarang);
        System.out.println("Jumlah Beli: " + jumlahBeli);
        System.out.println("Total: " + total());
    }
}

public class Tugas4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean validInput = false; // Flag untuk menandakan input yang valid

        while (!validInput) {
            try {
                System.out.print("Masukkan Kode Barang: ");
                String kodeBarang = scanner.nextLine();
                System.out.print("Masukkan Nama Barang: ");
                String namaBarang = scanner.nextLine();
                System.out.print("Masukkan Harga Barang: ");
                double hargaBarang = scanner.nextDouble();
                System.out.print("Masukkan Jumlah Beli: ");
                int jumlahBeli = scanner.nextInt();

                // Membuat objek Penjualan
                Penjualan penjualan = new Penjualan(kodeBarang, namaBarang, hargaBarang, jumlahBeli);
                penjualan.display();
                
                validInput = true; // Jika berhasil, set validInput ke true untuk keluar dari loop

            } catch (InputMismatchException e) {
                System.out.println("Error: Input tidak sesuai. Pastikan untuk memasukkan angka untuk harga dan jumlah beli.");
                scanner.nextLine(); // Menghapus input yang salah
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Terjadi kesalahan: " + e.getMessage());
            }
        }

        scanner.close(); // Menutup scanner untuk menghindari kebocoran sumber daya
    }
}