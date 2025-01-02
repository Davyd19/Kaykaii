import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// Class utama untuk manajemen ide konten
public class ManajemenIdeKonten {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContentManager manager = new ContentManager();

        while (true) {
            System.out.println("\n=== Sistem Manajemen Ide Konten ===");
            System.out.println("1. Tambah Ide Konten");
            System.out.println("2. Lihat Daftar Ide");
            System.out.println("3. Edit Ide Konten");
            System.out.println("4. Hapus Ide Konten");
            System.out.println("5. Filter Ide Berdasarkan Status");
            System.out.println("6. Cari Ide");
            System.out.println("7. Lihat Riwayat Perubahan");
            System.out.println("8. Keluar");
            System.out.println("9. Lihat Statistik Ide");

            System.out.print("Pilih menu: ");

            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer

            switch (pilihan) {
                case 1:
                    manager.tambahIde();
                    break;
                case 2:
                    manager.lihatSemuaIde();
                    break;
                case 3:
                    manager.editIde();
                    break;
                case 4:
                    manager.hapusIde();
                    break;
                case 5:
                    manager.filterIde();
                    break;
                case 6:
                    manager.cariIde();
                    break;
                case 7:
                    manager.lihatRiwayat();
                    break;
                case 8:
                    System.out.println("Keluar dari program. Terima kasih!");
                    return;
                case 9:
                    manager.lihatStatistik();
                    break;
                default:
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        }
    }
}

// Kelas untuk representasi ide konten
class ContentIdea {
    private int id;
    private String judul;
    private String deskripsi;
    private String kategori;
    private LocalDate tanggalEksekusi;
    private String status;

    public ContentIdea(String judul, String deskripsi, String kategori, LocalDate tanggalEksekusi, String status) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.kategori = kategori;
        this.tanggalEksekusi = tanggalEksekusi;
        this.status = status;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public LocalDate getTanggalEksekusi() {
        return tanggalEksekusi;
    }

    public void setTanggalEksekusi(LocalDate tanggalEksekusi) {
        this.tanggalEksekusi = tanggalEksekusi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

// Subclass untuk ide konten yang lebih detail
class DetailedContentIdea extends ContentIdea {
    private String additionalInfo;

    public DetailedContentIdea(String judul, String deskripsi, String kategori, LocalDate tanggalEksekusi,
            String status, String additionalInfo) {
        super(judul, deskripsi, kategori, tanggalEksekusi, status);
        this.additionalInfo = additionalInfo;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}

// Interface untuk operasi manajemen ide
interface IdeaOperations {
    void tambahIde();

    void lihatSemuaIde();

    void editIde();

    void hapusIde();

    void filterIde();

    void cariIde();

    void lihatRiwayat();
}

// Kelas untuk manajemen konten
class ContentManager implements IdeaOperations {
    private Connection koneksi;
    private Scanner scanner;
    private List<ContentIdea> ideList; // Menggunakan Collection Framework

    public ContentManager() {
        this.scanner = new Scanner(System.in);
        this.ideList = new ArrayList<>(); // Inisialisasi ArrayList untuk menyimpan ide
        try {
            // Mengatur koneksi ke database
            koneksi = DriverManager.getConnection("jdbc:postgresql://localhost:5432/manajemen_ide_konten", "postgres",
                    "Bandung1906");
            System.out.println("Koneksi berhasil!");
        } catch (SQLException e) {
            System.out.println("Gagal terhubung ke database: " + e.getMessage());
        }
    }

    @Override
    public void lihatSemuaIde() {
        try {
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ideas");

            if (!rs.isBeforeFirst()) { // Cek apakah ada data
                System.out.println("Tidak ada ide yang ditemukan.");
                return;
            }

            System.out.println("\n=== Daftar Ide Konten ===");
            while (rs.next()) {
                System.out.println("ID                : " + rs.getInt("id"));
                System.out.println("Judul             : " + rs.getString("judul"));
                System.out.println("Deskripsi         : " + rs.getString("deskripsi"));
                System.out.println("Kategori          : " + rs.getString("kategori"));
                System.out.println("Tanggal Eksekusi  : " + rs.getDate("tanggal_eksekusi"));
                System.out.println("Status            : " + rs.getString("status"));
                System.out.println("-----------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan ide: " + e.getMessage());
        }
    }

    @Override
    public void tambahIde() {
        try {
            System.out.print("Masukkan judul ide: ");
            String judul = scanner.nextLine();

            System.out.print("Masukkan deskripsi ide: ");
            String deskripsi = scanner.nextLine();

            System.out.print("Masukkan kategori ide: ");
            String kategori = scanner.nextLine();

            LocalDate tanggalEksekusi = null;
            while (tanggalEksekusi == null) {
                System.out.print("Masukkan tanggal eksekusi (yyyy-MM-dd): ");
                String tanggal = scanner.nextLine();
                try {
                    tanggalEksekusi = LocalDate.parse(tanggal, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    System.out.println("Format tanggal tidak valid. Silakan coba lagi.");
                }
            }

            String status = null;
            while (status == null) {
                System.out.print("Masukkan status (Draft/Dalam Proses/Selesai): ");
                String inputStatus = scanner.nextLine();
                if (inputStatus.equalsIgnoreCase("Draft") ||
                        inputStatus.equalsIgnoreCase("Dalam Proses") ||
                        inputStatus.equalsIgnoreCase("Selesai")) {
                    status = inputStatus;
                } else {
                    System.out.println("Status tidak valid. Harus 'Draft', 'Dalam Proses', atau 'Selesai'.");
                }
            }

            // Menambahkan ide baru ke dalam list
            ContentIdea newIdea = new ContentIdea(judul, deskripsi, kategori, tanggalEksekusi, status);
            ideList.add(newIdea);

            PreparedStatement cekJumlah = koneksi.prepareStatement(
                    "SELECT COUNT(*) FROM ideas WHERE tanggal_eksekusi = ?");
            cekJumlah.setDate(1, java.sql.Date.valueOf(tanggalEksekusi));
            ResultSet rs = cekJumlah.executeQuery();
            rs.next();
            if (rs.getInt(1) >= 3) {
                System.out.println("Maksimal 3 ide per hari. Pilih tanggal lain.");
                return;
            }

            PreparedStatement stmt = koneksi.prepareStatement(
                    "INSERT INTO ideas (judul, deskripsi, kategori, tanggal_eksekusi, status) VALUES (?, ?, ?, ?, ?)");
            stmt.setString(1, judul);
            stmt.setString(2, deskripsi);
            stmt.setString(3, kategori);
            stmt.setDate(4, java.sql.Date.valueOf(tanggalEksekusi));
            stmt.setString(5, status);
            stmt.executeUpdate();

            PreparedStatement riwayatStmt = koneksi.prepareStatement(
                    "INSERT INTO history (action, details) VALUES (?, ?)");
            riwayatStmt.setString(1, "Tambah Ide");
            riwayatStmt.setString(2, "Menambahkan ide: " + judul);
            riwayatStmt.executeUpdate();

            System.out.println("Ide berhasil ditambahkan!");
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan ide: " + e.getMessage());
        }
    }

    @Override
    public void editIde() {
        try {
            System.out.print("Masukkan ID ide yang ingin diedit: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer

            System.out.print("Masukkan judul baru: ");
            String judul = scanner.nextLine();

            System.out.print("Masukkan deskripsi baru: ");
            String deskripsi = scanner.nextLine();

            System.out.print("Masukkan kategori baru: ");
            String kategori = scanner.nextLine();

            LocalDate tanggalEksekusi = null;
            while (tanggalEksekusi == null) {
                System.out.print("Masukkan tanggal eksekusi baru (yyyy-MM-dd): ");
                String tanggal = scanner.nextLine();
                try {
                    tanggalEksekusi = LocalDate.parse(tanggal, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    System.out.println("Format tanggal tidak valid. Silakan coba lagi.");
                }
            }

            String status = null;
            while (status == null) {
                System.out.print("Masukkan status baru (Draft/Dalam Proses/Selesai): ");
                String inputStatus = scanner.nextLine();
                if (inputStatus.equalsIgnoreCase("Draft") ||
                        inputStatus.equalsIgnoreCase("Dalam Proses") ||
                        inputStatus.equalsIgnoreCase("Selesai")) {
                    status = inputStatus;
                } else {
                    System.out.println("Status tidak valid. Harus 'Draft', 'Dalam Proses', atau 'Selesai'.");
                }
            }

            PreparedStatement stmt = koneksi.prepareStatement(
                    "UPDATE ideas SET judul = ?, deskripsi = ?, kategori = ?, tanggal_eksekusi = ?, status = ? WHERE id = ?");
            stmt.setString(1, judul);
            stmt.setString(2, deskripsi);
            stmt.setString(3, kategori);
            stmt.setDate(4, java.sql.Date.valueOf(tanggalEksekusi));
            stmt.setString(5, status);
            stmt.setInt(6, id);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Ide berhasil diperbarui!");

                PreparedStatement riwayatStmt = koneksi.prepareStatement(
                        "INSERT INTO history (action, details) VALUES (?, ?)");
                riwayatStmt.setString(1, "Edit Ide");
                riwayatStmt.setString(2, "Mengedit ide dengan ID: " + id);
                riwayatStmt.executeUpdate();
            } else {
                System.out.println("Tidak ada ide dengan ID tersebut.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mengedit ide: " + e.getMessage());
        }
    }

    @Override
    public void hapusIde() {
        try {
            System.out.print("Masukkan ID ide yang ingin dihapus: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Membersihkan buffer

            PreparedStatement selectStmt = koneksi.prepareStatement("SELECT * FROM ideas WHERE id = ?");
            selectStmt.setInt(1, id);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String judul = rs.getString("judul");

                PreparedStatement stmt = koneksi.prepareStatement("DELETE FROM ideas WHERE id = ?");
                stmt.setInt(1, id);

                int rowsDeleted = stmt.executeUpdate();

                if (rowsDeleted > 0) {
                    System.out.println("Ide berhasil dihapus!");

                    PreparedStatement riwayatStmt = koneksi.prepareStatement(
                            "INSERT INTO history (action, details) VALUES (?, ?)");
                    riwayatStmt.setString(1, "Hapus Ide");
                    riwayatStmt.setString(2, "Menghapus ide: " + judul);
                    riwayatStmt.executeUpdate();
                }
            } else {
                System.out.println("Tidak ada ide dengan ID tersebut.");
            }
        } catch (SQLException e) {
            System.out.println("Gagal menghapus ide: " + e.getMessage());
        }
    }

    @Override
    public void filterIde() {
        try {
            System.out.print("Masukkan status untuk memfilter (Draft/Dalam Proses/Selesai): ");
            String status = scanner.nextLine();

            PreparedStatement stmt = koneksi.prepareStatement("SELECT * FROM ideas WHERE status = ?");
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();

            System.out.println("Hasil filter ide dengan status: " + status);
            System.out.println("---------------------------------------------------");

            int nomor = 1; // Nomor urut untuk daftar
            while (rs.next()) {
                System.out.println(nomor + ". Judul: " + rs.getString("judul"));
                System.out.println("   Deskripsi   : " + rs.getString("deskripsi"));
                System.out.println("   Kategori    : " + rs.getString("kategori"));
                System.out.println("   Tanggal Eksekusi : " + rs.getDate("tanggal_eksekusi"));
                System.out.println("   Status      : " + rs.getString("status"));
                System.out.println("---------------------------------------------------");
                nomor++;
            }

            if (nomor == 1) {
                System.out.println("Tidak ada ide dengan status tersebut.");
            }

        } catch (SQLException e) {
            System.out.println("Gagal memfilter ide: " + e.getMessage());
        }
    }

    @Override
    public void cariIde() {
        try {
            System.out.print("Masukkan kata kunci untuk mencari ide: ");
            String keyword = scanner.nextLine();
            PreparedStatement pstmt = koneksi.prepareStatement(
                    "SELECT * FROM ideas WHERE judul LIKE ? OR deskripsi LIKE ?");
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.out.println("Tidak ada ide yang cocok dengan kata kunci: " + keyword);
                return;
            }

            System.out.println("\n=== Hasil Pencarian Ide ===");
            while (rs.next()) {
                System.out.println("ID                : " + rs.getInt("id"));
                System.out.println("Judul             : " + rs.getString("judul"));
                System.out.println("Deskripsi         : " + rs.getString("deskripsi"));
                System.out.println("Kategori          : " + rs.getString("kategori"));
                System.out.println("Tanggal Eksekusi  : " + rs.getDate("tanggal_eksekusi"));
                System.out.println("Status            : " + rs.getString("status"));
                System.out.println("-----------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Gagal mencari ide: " + e.getMessage());
        }
    }

    @Override
    public void lihatRiwayat() {
        try {
            Statement stmt = koneksi.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM history");

            System.out.println("Riwayat Perubahan:");
            System.out.println("---------------------------------------------------");

            int nomor = 1; // Nomor urut untuk daftar
            while (rs.next()) {
                System.out.println(nomor + ". Aksi  : " + rs.getString("action"));
                System.out.println("   Detail: " + rs.getString("details"));
                System.out.println("   Waktu : " + rs.getTimestamp("timestamp"));
                System.out.println("---------------------------------------------------");
                nomor++;
            }

            if (nomor == 1) {
                System.out.println("Tidak ada riwayat yang ditemukan.");
            }

        } catch (SQLException e) {
            System.out.println("Gagal melihat riwayat: " + e.getMessage());
        }
    }

    public void lihatStatistik() {
        try {
            System.out.println("\n=== Statistik Ide ===");

            System.out.println("\nJumlah ide berdasarkan status:");
            PreparedStatement stmtStatus = koneksi.prepareStatement(
                    "SELECT status, COUNT(*) AS jumlah FROM ideas GROUP BY status");
            ResultSet rsStatus = stmtStatus.executeQuery();
            while (rsStatus.next()) {
                System.out.printf("Status: %-12s | Jumlah: %d\n",
                        rsStatus.getString("status"), rsStatus.getInt("jumlah"));
            }

            System.out.println("\nJumlah ide berdasarkan kategori:");
            PreparedStatement stmtKategori = koneksi.prepareStatement(
                    "SELECT kategori, COUNT(*) AS jumlah FROM ideas GROUP BY kategori");
            ResultSet rsKategori = stmtKategori.executeQuery();
            while (rsKategori.next()) {
                System.out.printf("Kategori: %-15s | Jumlah: %d\n",
                        rsKategori.getString("kategori"), rsKategori.getInt("jumlah"));
            }

        } catch (SQLException e) {
            System.out.println("Gagal menampilkan statistik: " + e.getMessage());
        }
    }
}
