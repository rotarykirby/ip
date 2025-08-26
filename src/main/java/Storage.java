import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public List<Task> loadTasks() throws LebronException {
        List<Task> taskList = new ArrayList<>();
        Path p = Paths.get(filePath);

        try {
            // Ensure ./data exists, and create an empty save file on first run
            Files.createDirectories(p.getParent());
            if (!Files.exists(p)) {
                Files.createFile(p);
                return taskList; // first run -> empty
            }
        } catch (IOException e) {
            throw new LebronException("Error - Cannot prepare save file: " + e.getMessage());
        }

        try (Scanner scanner = new Scanner(p, java.nio.charset.StandardCharsets.UTF_8)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task t = parseTask(line);
                if (t != null) taskList.add(t);
            }
            return taskList;
        } catch (IOException e) {
            throw new LebronException("Error - Failed to load tasks: " + e.getMessage());
        }
    }


    public void saveTasks(List<Task> taskList) throws LebronException {
        Path p = Paths.get(filePath);

        try {
            Files.createDirectories(p.getParent());
            try (FileWriter fw = new FileWriter(filePath, java.nio.charset.StandardCharsets.UTF_8)) {
                for (Task task : taskList) {
                    fw.write(formatToWrite(task));
                    fw.write("\n");
                }
            }
        } catch (IOException e) {
            throw new LebronException("Error - Failed to save tasks: " + e.getMessage());
        }
    }

    private Task parseTask(String s) throws LebronException {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }

        String[] parts = s.split(" \\| ");
        // check for invalid format
        if (parts.length < 3) {
            return null;
        }

        Task t = null;
        String type = parts[0].trim();
        boolean isDone = parts[1].trim().equals("1");
        String desc = parts[2].trim();

        t = switch (type) {
            case "T" -> new Todo(desc);
            case "D" -> {
                if (parts.length < 4) {
                    throw new LebronException("Error - Unable to save line (deadline missing /by): " + s);
                }
                yield new Deadline(desc, parts[3].trim());
            }
            case "E" -> {
                if (parts.length < 4) {
                    throw new LebronException("Error - Unable to save line (event missing times): " + s);
                }
                String[] fromTo = parts[3].trim().split("–", 2);
                yield new Event(desc, fromTo[0].trim(), fromTo[1].trim());
            }
            default -> t;
        };

        if (t != null && isDone) {
            t.markDone();
        }
        return t;
    }

    private String formatToWrite(Task t) {
        String taskType;
        boolean isDone = t.getIsDone();

        if (t instanceof Todo) {
            taskType = "T";
            return taskType + " | " + isDone + " | " + t.getDescription();
        } else if (t instanceof Deadline) {
            taskType = "D";
            String by = ((Deadline) t).getOriginalBy();
            return taskType + " | " + isDone + " | " + t.getDescription() + " | " + by;
        } else if (t instanceof Event) {
            taskType = "E";
            String from = ((Event) t).getOriginalFrom();
            String to = ((Event) t).getOriginalTo();
            return taskType + " | " + isDone + " | " + t.getDescription() + " | " + from + " – " + to;
        } else {
            // should be unreachable?
            taskType = "T";
            return taskType + " | " + isDone + " | " + t.getDescription();
        }
    }
}
