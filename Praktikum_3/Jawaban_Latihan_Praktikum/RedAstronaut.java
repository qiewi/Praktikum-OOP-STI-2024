// NAMA = Rajendra Farras Rayhan
// NIM = 18222105
// TANGGAL = 04/06/2024
import java.util.Arrays;

// Lengkapi definisi class
public class RedAstronaut extends Player implements Impostor{
    // Atribut skill bertipe data String
    private String skill;

    // Atribut DEFAULT_SKILL bertipe data String dengan nilai "experienced"
    private static final String DEFAULT_SKILL = "experienced";

    public RedAstronaut(String name) {
        // Panggil constructor dengan parameter name, DEFAULT_SUS_LEVEL, dan DEFAULT_SKILL
        this(name, DEFAULT_SUS_LEVEL, DEFAULT_SKILL);
    }

    public RedAstronaut(String name, int susLevel, String skill) {
        // Panggil constructor dari superclass dengan parameter name dan susLevel
        super(name, susLevel);
        // Isi variabel skill dengan parameter skill yang sudah diubah menjadi lowercase
        this.skill = skill.toLowerCase();
    }

    /*
        * Player yang frozen tidak bisa melakukan emergency meeting.
        * Mengadakan meeting dan memilih untuk mengeluarkan (freeze) Player yang paling mencurigakan, hanya mempertimbangkan Player yang tidak frozen
            * Player dengan susLevel tertinggi (yang BUKAN Impostor yang saat ini yang memanggil pertemuan) akan dituduh sebagai Impostor dan akan dikeluarkan
            * Jika dua pemain memiliki susLevel tertinggi yang sama, tidak ada pemain yang akan dikeluarkan.
            * **Hint**: Tidak perlu mengiterasi seluruh array.
        * Pastikan untuk mengubah variabel frozen dari pemain menjadi true saat mengeluarkan pemain (jangan memanggil metode "freeze"!)
        * Di akhir pemungutan suara, periksa apakah permainan telah berakhir menggunakan metode yang disediakan di Player.java
        * Tidak mengembalikan apapun
    */
    public void emergencyMeeting() {
        if (isFrozen()){
            return; // Tidak melakukan apapun kalau frozen
        }

        Player[] players = Player.getPlayers();
        int indexPemainTerSus = -1;
        int maxSusLevel = 0;
        boolean seri = false;

        for (int i = 0; i < players.length; i++){
            Player currentPlayer = players[i];
            // Cek currentPlayer gak frozen, bukan yang manggil, dan level sus nya paling tinggi.
            if (!currentPlayer.isFrozen() && currentPlayer != this && currentPlayer.getSusLevel() > maxSusLevel) {
                maxSusLevel = currentPlayer.getSusLevel();
                indexPemainTerSus = i;
                seri = false; // Reset variabel seri karena ada yang paling tinggi.
            }
            else if (currentPlayer.getSusLevel() == maxSusLevel && currentPlayer != this) {
                seri = true; // Ketemu level sus tertinggi lebih dari satu.
                break; // Keluar dari loop soalnya kalo seri, gak ada aksi yang dilakuin.
            }
        }

        // Freeze player ter-Sus kalo engga seri.
        if (!seri && indexPemainTerSus != -1) {
            players[indexPemainTerSus].setFrozen(true);
        }
    
        gameOver(); // Cek kalo emergency meeting kali ini bakal selesaiin permainan.
    }

    /*
        * Mengimplementasikan metode yang disediakan di antarmuka Impostor.
        * Tidak mungkin untuk melakukan freeze Impostor lain, dan Impostor yang frozen tidak dapat mencoba untuk melakukan freeze. Jika Player yang diberikan Impostor, metode harus berakhir. Melakukan freeze pada Player yang sudah beku juga tidak berpengaruh apa-apa.
        * Freeze berhasil jika susLevel RedAstronaut lebih rendah dari Player tersebut
        * Jika freeze tidak berhasil, susLevel RedAstronaut berlipat ganda (kalikan susLevel saat ini dengan 2)
        * Ingat untuk mengubah nilai boolean frozen untuk Crewmate sesuai kebutuhan.
        * Setelah percobaan freeze, periksa apakah permainan telah berakhir menggunakan metode yang disediakan di Player.java
        * Tidak mengembalikan apapun
    */
    public void freeze(Player p) {
        if (p instanceof RedAstronaut || this.isFrozen() || p.isFrozen()) {
            return;
        }
        if (this.getSusLevel() < p.getSusLevel()) {
            p.setFrozen(true);
        } else {
            this.setSusLevel(this.getSusLevel() * 2);
        }
        gameOver();
    }

    /*
        * Tidak mungkin menyabotase Impostor lain, dan Impostor yang frozen tidak dapat menyabotase. Menyabotase Player yang frozen tidak akan berpengaruh apa-apa.
        * Jika susLevel Impostor di bawah 20, mereka meningkatkan susLevel Crewmate sebesar 50%
        * Jika tidak, mereka hanya bisa meningkatkan susLevel Crewmate sebesar 25%
        * (Catatan: Dalam kedua kasus, susLevel Crewmate dibulatkan ke bawah ke nilai int terdekat)
        * Tidak mengembalikan apapun
    */
    public void sabotage(Player p) {
        if (!isFrozen() && p instanceof BlueAstronaut){
            if(getSusLevel() < 20) {
                p.setSusLevel((int) (p.getSusLevel()*1.5));
            }
            else {
                p.setSusLevel((int) (p.getSusLevel()*1.25));
            }
        }
    }

    /*
        * Dua Red sama jika mereka keduanya memiliki nama, frozen, susLevel, dan skill yang sama
        * Mengembalikan sebuah boolean
    */
    public boolean equals(Object o) {
        if (o instanceof RedAstronaut) {
            RedAstronaut redplayer = (RedAstronaut)o;
            return super.equals(redplayer) && this.skill == redplayer.skill;
        }
        return false;
    }

    /*
        Mengembalikan String yang menggambarkan RedAstronaut sebagai berikut:
        * `"My name is [name], and I have a `susLevel` of [susLevel]. I am currently (frozen / not frozen). I am an [skill] player!"`
        * Jika susLevel lebih besar dari 15, kembalikan keluaran dalam huruf kapital semua. (Hint: toUppercase)
        * (Note: gantikan nilai dalam tanda kurung [] dengan nilai sebenarnya)
        * Anda harus menggunakan metode toString() dari kelas Player.
    */
    public String toString() {
        String redString = super.toString() + " I am an " + this.skill + " player!";
        return getSusLevel() > 15 ? redString.toUpperCase() : redString;
    }
}
