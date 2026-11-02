import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

// Helper class untuk konversi & play MP3 sebagai WAV menggunakan AudioSystem
// Untuk JDK 17 tanpa JavaFX, kita buat WAV fallback
public class ConvertToWav {
    public static void main(String[] args) throws Exception {
        // Coba buka file MP3 langsung - kalau provider MP3 ada, ini akan berhasil
        // Kalau tidak, kita perlu approach lain
        String mp3Path = "src/Assets/prikitiw.mp3";
        String wavPath = "src/Assets/prikitiw.wav";
        
        File mp3File = new File(mp3Path);
        System.out.println("MP3 exists: " + mp3File.exists());
        
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(mp3File);
            AudioFormat baseFormat = ais.getFormat();
            System.out.println("Format: " + baseFormat);
            
            AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                baseFormat.getSampleRate(),
                16,
                baseFormat.getChannels(),
                baseFormat.getChannels() * 2,
                baseFormat.getSampleRate(),
                false
            );
            
            AudioInputStream decodedAis = AudioSystem.getAudioInputStream(decodedFormat, ais);
            AudioSystem.write(decodedAis, AudioFileFormat.Type.WAVE, new File(wavPath));
            System.out.println("WAV file created: " + new File(wavPath).exists());
            
            decodedAis.close();
            ais.close();
        } catch (UnsupportedAudioFileException e) {
            System.out.println("MP3 not supported by AudioSystem (no SPI provider). Need WAV file directly.");
            System.out.println("Please manually convert prikitiw.mp3 to prikitiw.wav");
        }
    }
}
