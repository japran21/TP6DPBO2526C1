$inputFile = "C:\Users\ilalz\IdeaProjects\FlappyBird\src\Assets\prikitiw.mp3"
$outputFile = "C:\Users\ilalz\IdeaProjects\FlappyBird\src\Assets\prikitiw.wav"

Add-Type -AssemblyName PresentationCore

$uri = New-Object System.Uri($inputFile)
$player = New-Object System.Windows.Media.MediaPlayer
$player.Open($uri)

# Wait for media to open
Start-Sleep -Milliseconds 1500

# Use Shell.Application to convert
$shell = New-Object -ComObject Shell.Application

# Alternative: use mciSendString directly
Add-Type -TypeDefinition @"
using System;
using System.Text;
using System.Runtime.InteropServices;

public class MCI {
    [DllImport("winmm.dll")]
    public static extern int mciSendString(string command, StringBuilder returnValue, int returnLength, IntPtr callback);
}
"@

$sb = New-Object System.Text.StringBuilder 256
[MCI]::mciSendString("open `"$inputFile`" type mpegvideo alias mp3file", $sb, 256, [IntPtr]::Zero)
[MCI]::mciSendString("save mp3file `"$outputFile`"", $sb, 256, [IntPtr]::Zero)
[MCI]::mciSendString("close mp3file", $sb, 256, [IntPtr]::Zero)

if (Test-Path $outputFile) {
    Write-Output "SUCCESS: WAV file created at $outputFile"
} else {
    Write-Output "FAILED: WAV file was not created"
}
