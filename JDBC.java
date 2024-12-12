import java.util.*;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

public class JDBC {
    private static Scanner scanner = new Scanner(System.in);
    private static TreeMap<String, Penjualan> penjualanMap = new TreeMap<>();

    public static void main(String[] args) {
        while (!login()) {
            System.out.println("Login gagal. Silakan coba lagi.\n");
        }

        String namaKasir = "Davyd Yehuda";
        System.out.println("\nSelamat Datang di Supermarket");
        displayTanggalDanWaktu();
        System.out.println("+----------------------------------------------------+");

        while (true) {
            System.out.println("1. Transaksi");
            System.out.println("2. Lihat Daftar Penjualan");
            System.out.println("3. Cari Penjualan");
            System.out.println("4. Hapus Penjualan");
            System.out.println("5. Update Penjualan");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu: ");
            int menu = scanner.nextInt();
            scanner.nextLine(); // Menghapus newline yang tersisa

            switch (menu) {
                case 1:
                    inputFaktur(namaKasir);
                    break;
                case 2:
                    lihatDaftarPenjualan();
                    break;
                case 3:
                    cariPenjualan();
                    break;
                case 4:
                    hapusPenjualan();
                    break;
                case 5:
                    updatePenjualan();
                    break;
                case 6:
                    System.out.println("Terima kasih telah berbelanja!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Menu tidak valid. Silakan coba lagi.");
            }
        }
    }

    private static void displayTanggalDanWaktu() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy HH:mm:ss");
        System.out.println("Tanggal dan Waktu: " + now.format(formatter));
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

        if (inputUsername.equals(username) && inputPassword.equals(password)
                && inputCaptcha.equalsIgnoreCase(captcha)) {
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
            captcha.append(characters.charAt((int) (Math.random() * characters.length())));
        }
        return captcha.toString();
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
        penjualanMap.put(kodeBarang, penjualan);
    }

    private static void lihatDaftarPenjualan() {
        if (penjualanMap.isEmpty()) {
            System.out.println("Tidak ada penjualan.");
        } else {
            for (Penjualan penjualan : penjualanMap.values()) {
                penjualan.display("Davyd Yehuda");
            }
        }
    }

    private static void cariPenjualan() {
        System.out.print("Masukkan Kode Barang: ");
        String kodeBarang = scanner.nextLine();

        if (penjualanMap.containsKey(kodeBarang)) {
            Penjualan penjualan = penjualanMap.get(kodeBarang);
            penjualan.display("Davyd Yehuda");
        } else {
            System.out.println("Penjualan tidak ditemukan.");
        }
    }

    private static void hapusPenjualan() {
        System.out.print("Masukkan Kode Barang: ");
        String kodeBarang = scanner.nextLine();

        if (penjualanMap.containsKey(kodeBarang)) {
            penjualanMap.remove(kodeBarang);
            System.out.println("Penjualan berhasil dihapus.");
        } else {
            System.out.println("Penjualan tidak ditemukan.");
        }
    }

    private static void updatePenjualan() {
        System.out.print("Masukkan Kode Barang: ");
        String kodeBarang = scanner.nextLine();

        if (penjualanMap.containsKey(kodeBarang)) {
            Penjualan penjualan = penjualanMap.get(kodeBarang);
            System.out.print("Masukkan Nama Barang Baru: ");
            String namaBarang = scanner.nextLine();
            System.out.print("Masukkan Harga Barang Baru: ");
            double hargaBarang = scanner.nextDouble();
            System.out.print("Masukkan Jumlah Beli Baru: ");
            int jumlahBeli = scanner.nextInt();
            scanner.nextLine(); // Menghapus newline yang tersisa

            penjualan = new Penjualan(kodeBarang, namaBarang, hargaBarang, jumlahBeli);
            penjualanMap.put(kodeBarang, penjualan);
            System.out.println("Penjualan berhasil diupdate.");
        } else {
            System.out.println("Penjualan tidak ditemukan.");
        }
    }
}