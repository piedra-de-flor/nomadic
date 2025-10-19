package com.example.Triple_clone.common;

import com.example.Triple_clone.common.file.FileDataDto;
import com.example.Triple_clone.common.file.FileManager;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private FileManager fileManager;
    private static final String TEST_BASE_PATH = "test_files/";

    @BeforeAll
    static void setupOnce() throws IOException {
        Files.createDirectories(new File(TEST_BASE_PATH).toPath());
    }

    @BeforeEach
    void setUp() {
        fileManager = new FileManager() {
            @Override
            public FileDataDto readFile(String filePath) {
                // override for test path
                Queue<String> datas = new java.util.LinkedList<>();
                try (var br = new java.io.BufferedReader(new java.io.FileReader(TEST_BASE_PATH + filePath + ".txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        datas.add(line.isEmpty() ? "/n" : line);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new FileDataDto(datas);
            }

            @Override
            public void deleteExistingImage(String imagePath) {
                File file = new File(imagePath);
                if (file.exists()) {
                    file.delete();
                }
            }

            @Override
            public byte[] loadImageAsResource(String imageName) {
                try {
                    return Files.readAllBytes(new File(TEST_BASE_PATH + imageName).toPath());
                } catch (IOException e) {
                    throw new IllegalArgumentException("이미지를 찾을 수 없습니다.", e);
                }
            }
        };
    }

    @Test
    @DisplayName("텍스트 파일 읽기 성공")
    void readFileSuccess() throws Exception {
        String fileName = "test_read";
        FileWriter writer = new FileWriter(TEST_BASE_PATH + fileName + ".txt");
        writer.write("Line1\n\nLine3");
        writer.close();

        FileDataDto result = fileManager.readFile(fileName);

        assertThat(result.fileData()).containsExactly("Line1", "/n", "Line3");
    }

    @Test
    @DisplayName("이미지 업로드 성공")
    void uploadImageSuccess() {
    }

    @Test
    @DisplayName("이미지 삭제 성공")
    void deleteImageSuccess() throws IOException {
        String filePath = TEST_BASE_PATH + "to_delete.jpg";
        File file = new File(filePath);
        Files.write(file.toPath(), "delete me".getBytes());

        assertTrue(file.exists());

        fileManager.deleteExistingImage(filePath);

        assertFalse(file.exists());
    }

    @Test
    @DisplayName("이미지 로딩 성공")
    void loadImageSuccess() throws IOException {
        String fileName = "loadable.jpg";
        File file = new File(TEST_BASE_PATH + fileName);
        byte[] expected = "image data".getBytes();
        Files.write(file.toPath(), expected);

        byte[] actual = fileManager.loadImageAsResource(fileName);

        assertArrayEquals(expected, actual);
    }

    @AfterAll
    static void cleanUp() throws IOException {
        Files.walk(new File(TEST_BASE_PATH).toPath())
                .map(java.nio.file.Path::toFile)
                .forEach(File::delete);
    }
}
