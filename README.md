# TP6DPBO2526C1

# JANJI
Saya Muhammad Ilal Zhafran dengan NIM 2502623 mengerjakan Tugas Praktikum 6 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

# Desain Program

## App.Java
- Berfungsi sebagai titik masuk (entry point) aplikasi.
- Menginisialisasi JFrame sebagai jendela utama aplikasi dengan ukuran 360x640 piksel.
- Menghubungkan komponen logika (Logic) dengan tampilan antarmuka (View) serta mengatur fokus agar input keyboard dapat diterima dengan baik.

## Logic.Java
- Mengatur seluruh mekanisme dan aturan permainan (gerakan gravitasi, kecepatan pipa, skor, pengecekan tabrakan/kolisi, serta status game seperti menu, playing, dan game over).
- Mengimplementasikan ActionListener untuk menjalankan game loop (60 FPS) dan timer kemunculan pipa (pipe spawner).
- Mengimplementasikan KeyListener untuk mendeteksi penekanan tombol pengguna (tombol Spasi untuk melompat dan tombol R untuk mereset permainan saat game over).
- Mengelola pemutaran musik latar (soundtrack) menggunakan Clip.

## View.Java
- Berfungsi sebagai kanvas grafis tempat seluruh elemen visual digambar ulang menggunakan metode paintComponent() dan draw().
- Berisi komponen GUI berbasis Swing seperti judul game, tombol Mulai Permainan, tombol Keluar, serta label skor.

## Player.Java
- Merepresentasikan objek pemain (tengkorak).
- Menyimpan atribut posisi (posX, posY), ukuran (width, height), gambar aset, serta kecepatan vertikal

## Pipe.Java
- Merepresentasikan objek rintangan (pipa atas dan pipa bawah).
- Menyimpan atribut posisi, ukuran, status apakah pipa sudah dilewati (passed), dan penanda apakah pipa tersebut dapat dihitung sebagai skor (scorable).

# ALUR

## Inisialisasi Aplikasi (App.java)
- Program dijalankan melalui method main(). JFrame dibuat dengan ukuran 360x640 piksel, posisi di tengah layar, dan tidak dapat diubah ukurannya (resizable = false).
- Objek Logic diinisialisasi (memuat gambar latar belakang, gambar pemain, gambar pipa, dan menyiapkan file audio soundtrack).
- Objek View dibuat dengan memasukkan instance Logic ke dalamnya, lalu ditambahkan ke dalam JFrame.

## Tampilan Menu Utama (View.java)
- Saat pertama kali dibuka, game menampilkan menu utama yang berisi judul game, tombol Mulai Permainan, dan tombol Keluar.
- Latar belakang dan menu digambar, sementara game loop utama belum berjalan (gameStarted = false).

## Memulai Permainan
- Pengguna mengklik tombol Mulai Permainan, yang memicu fungsi logic.startGame().
- Status gameStarted berubah menjadi true, label menu disembunyikan, skor diatur ke angka 0, musik latar diputar secara berulang (loop), serta timer untuk game loop dan pemunculan pipa (pipeSpawner) mulai berjalan.

## Loop Permainan & Pergerakan (Logic.java)
- Setiap tick waktu (60 frame per detik), method move() dipanggil secara otomatis:
- Player: Dipengaruhi oleh gravitasi (velocityY bertambah terus), membuat pemain jatuh ke bawah jika tidak ada input.
- Pipe: Pipa-pipa bergerak ke arah kiri layar (velocityX). Pipa yang sudah keluar dari batas layar akan dihapus otomatis dari memori (ArrayList).
- Skor: Jika posisi pipa melewati posisi pemain dan belum pernah dihitung (!pipe.isPassed()), maka skor akan bertambah +1 dan label skor diperbarui.
- Tabrakan (Collision): Program mengecek apakah kotak pembatas (bounding box) pemain beririsan dengan kotak pembatas pipa menggunakan fungsi collision(), atau jika pemain jatuh menabrak batas bawah layar.

## Kondisi Game Over
- Jika terjadi tabrakan atau pemain jatuh ke bawah, status gameOver menjadi true.
- Timer permainan, spawner pipa, dan musik latar dihentikan.
- Layar menampilkan kotak dialog semi-transparan bertuliskan GAME OVER beserta skor akhir dan instruksi "Tekan R untuk restart".

## Restart atau Keluar
- Jika pemain menekan tombol R pada keyboard saat kondisi Game Over, fungsi resetGame() dipanggil untuk mengembalikan posisi pemain, mengosongkan pipa, mereset skor ke 0, dan memulai ulang musik serta game loop.
- Jika pemain menekan tombol Keluar di menu utama, aplikasi akan tertutup secara otomatis (System.exit(0)).

# DOKUMENTASI
<img width="447" height="825" alt="image" src="https://github.com/user-attachments/assets/3f83b170-3eb1-492a-a0ed-7dcd7621cf6a" />
<img width="442" height="830" alt="image" src="https://github.com/user-attachments/assets/b40f0fdf-7f65-4f89-ad21-b8bd717d39ca" />
<img width="447" height="825" alt="image" src="https://github.com/user-attachments/assets/1e73c451-b68c-4a81-9ba9-3e1bd0823fe9" />

# Vidio
ada di folder dokumentasi


