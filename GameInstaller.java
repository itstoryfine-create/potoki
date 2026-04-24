import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GameInstaller {

    // Укажите путь к папке Games (создайте её вручную перед запуском)
    private static final String GAMES_DIR = "D:/Games"; // Для Windows
    // private static final String GAMES_DIR = "/Users/admin/Games"; // Для macOS

    public static void main(String[] args) {
        // Корневая директория Games
        File gamesFolder = new File(GAMES_DIR);
        if (!gamesFolder.exists()) {
            System.err.println("Папка Games не существует по пути: " + GAMES_DIR);
            System.err.println("Пожалуйста, создайте её вручную и повторите запуск.");
            return;
        }

        // Лог для записи в temp.txt
        StringBuilder log = new StringBuilder();

        // 1. Создание основных директорий: src, res, savegames, temp
        createDirectory(gamesFolder, "src", log);
        createDirectory(gamesFolder, "res", log);
        createDirectory(gamesFolder, "savegames", log);
        createDirectory(gamesFolder, "temp", log);

        // 2. Внутри src: main и test
        File srcDir = new File(gamesFolder, "src");
        createDirectory(srcDir, "main", log);
        createDirectory(srcDir, "test", log);

        // 3. Внутри main: Main.java, Utils.java
        File mainDir = new File(srcDir, "main");
        createFile(mainDir, "Main.java", log);
        createFile(mainDir, "Utils.java", log);

        // 4. Внутри res: drawables, vectors, icons
        File resDir = new File(gamesFolder, "res");
        createDirectory(resDir, "drawables", log);
        createDirectory(resDir, "vectors", log);
        createDirectory(resDir, "icons", log);

        // 5. Запись лога в temp.txt
        File tempDir = new File(gamesFolder, "temp");
        File tempFile = new File(tempDir, "temp.txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(log.toString());
            System.out.println("Лог успешно записан в " + tempFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Ошибка при записи в temp.txt: " + e.getMessage());
            log.append("Ошибка при записи лога: ").append(e.getMessage()).append("\n");
        }

        // Вывод лога в консоль для наглядности
        System.out.println("\n--- Лог установки ---");
        System.out.print(log);
    }

    /**
     * Создаёт директорию внутри родительской.
     * @param parent родительская директория
     * @param name имя новой директории
     * @param log StringBuilder для накопления лога
     */
    private static void createDirectory(File parent, String name, StringBuilder log) {
        File dir = new File(parent, name);
        boolean created = dir.mkdir();
        if (created) {
            log.append("Каталог создан: ").append(dir.getAbsolutePath()).append("\n");
        } else if (dir.exists()) {
            log.append("Каталог уже существует: ").append(dir.getAbsolutePath()).append("\n");
        } else {
            log.append("НЕ УДАЛОСЬ создать каталог: ").append(dir.getAbsolutePath()).append("\n");
        }
    }

    /**
     * Создаёт файл внутри указанной директории.
     * @param parent родительская директория
     * @param name имя файла
     * @param log StringBuilder для накопления лога
     */
    private static void createFile(File parent, String name, StringBuilder log) {
        File file = new File(parent, name);
        try {
            boolean created = file.createNewFile();
            if (created) {
                log.append("Файл создан: ").append(file.getAbsolutePath()).append("\n");
            } else if (file.exists()) {
                log.append("Файл уже существует: ").append(file.getAbsolutePath()).append("\n");
            } else {
                log.append("НЕ УДАЛОСЬ создать файл: ").append(file.getAbsolutePath()).append("\n");
            }
        } catch (IOException e) {
            log.append("Ошибка при создании файла ").append(file.getAbsolutePath())
                    .append(": ").append(e.getMessage()).append("\n");
        }
    }
}
