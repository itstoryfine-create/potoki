import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GameSaver {

    private static final String GAMES_DIR = "D:/Games";

    public static void main(String[] args) {
        // Папка savegames
        String savegamesPath = GAMES_DIR + File.separator + "savegames";
        File savegamesDir = new File(savegamesPath);
        if (!savegamesDir.exists()) {
            System.err.println("Папка savegames не найдена: " + savegamesPath);
            return;
        }

        // 1. Создаём три экземпляра GameProgress
        GameProgress progress1 = new GameProgress(100, 5, 1, 10.5);
        GameProgress progress2 = new GameProgress(80, 8, 2, 25.3);
        GameProgress progress3 = new GameProgress(50, 12, 3, 47.8);

        // Список файлов для сохранения
        List<String> saveFiles = new ArrayList<>();

        // 2. Сохраняем объекты в файлы
        String save1 = savegamesPath + File.separator + "save1.dat";
        String save2 = savegamesPath + File.separator + "save2.dat";
        String save3 = savegamesPath + File.separator + "save3.dat";

        saveGame(save1, progress1);
        saveGame(save2, progress2);
        saveGame(save3, progress3);

        saveFiles.add(save1);
        saveFiles.add(save2);
        saveFiles.add(save3);

        // 3. Запаковываем файлы в архив
        String zipPath = savegamesPath + File.separator + "saves.zip";
        zipFiles(zipPath, saveFiles);

        // 4. Удаляем исходные файлы .dat
        for (String filePath : saveFiles) {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Удалён файл: " + filePath);
                } else {
                    System.err.println("Не удалось удалить файл: " + filePath);
                }
            }
        }
    }

    /**
     * Сохраняет объект GameProgress в файл по указанному пути.
     * @param filePath полный путь к файлу сохранения
     * @param progress объект прогресса игры
     */
    public static void saveGame(String filePath, GameProgress progress) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(progress);
            System.out.println("Сохранение создано: " + filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при сохранении " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Упаковывает список файлов в zip-архив.
     * @param zipPath полный путь к создаваемому zip-архиву
     * @param files список полных путей к файлам, которые нужно добавить в архив
     */
    public static void zipFiles(String zipPath, List<String> files) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filePath : files) {
                File file = new File(filePath);
                if (!file.exists()) {
                    System.err.println("Файл не найден, пропускаем: " + filePath);
                    continue;
                }
                // Добавляем запись в архив с именем файла (без пути)
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    System.out.println("Добавлен в архив: " + file.getName());
                } catch (IOException e) {
                    System.err.println("Ошибка при добавлении файла " + filePath + ": " + e.getMessage());
                }
            }
            System.out.println("Архив создан: " + zipPath);
        } catch (IOException e) {
            System.err.println("Ошибка при создании архива: " + e.getMessage());
        }
    }
}
