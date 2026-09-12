package edu.ccrm.io;

import java.nio.file.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.*;

public class BackupService {

    // Create a timestamped backup of exported files
    public Path backupExportedFiles(Path exportsFolder) throws IOException {
        if (!Files.exists(exportsFolder)) throw new IOException("Exports folder does not exist: " + exportsFolder);
        String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path backup = exportsFolder.resolveSibling("backup_" + stamp);
        Files.createDirectories(backup);

        try (Stream<Path> stream = Files.walk(exportsFolder)) {
            stream.filter(Files::isRegularFile).forEach(p -> {
                try {
                    Path target = backup.resolve(exportsFolder.relativize(p));
                    Files.createDirectories(target.getParent());
                    Files.copy(p, target, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) { throw new UncheckedIOException(e); }
            });
        }
        return backup;
    }

    // Compute total size of folder
    public long directorySizeRecursive(Path p) throws IOException {
        try (Stream<Path> stream = Files.walk(p)) {
            return stream.filter(Files::isRegularFile).mapToLong(fp -> {
                try { return Files.size(fp); } catch (IOException e) { throw new UncheckedIOException(e); }
            }).sum();
        }
    }
}
