package utils;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.util.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Kuber {

    private static final Logger logger = LogManager.getLogger(Kuber.class);

    public Kuber() {

    }
    public static void downloadLogs(String podName, String containerName, String kubeConfigPath) {
        try {
            //Создание даты
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
            String isoDate = sdf.format(date);

            // Инициализация клиента Kubernetes с использованием файла конфигурации
            ApiClient client = Config.fromConfig(kubeConfigPath);
            Configuration.setDefaultApiClient(client);

            // Использование API для работы с Kubernetes
            CoreV1Api api = new CoreV1Api();

            // Поиск поды
            V1Pod pod = api.readNamespacedPod(podName, "default", null);
            if (pod != null) {
                // Чтение логов
                String namespace = Objects.requireNonNull(pod.getMetadata()).getNamespace();

                String logs = api.readNamespacedPodLog(podName, namespace, containerName, null, null, null, null, null, null, null, null);

                // Запись логов в файл
                String fileName = "logs-" + podName + "-" + containerName + "-" + isoDate + ".txt"; // имя файла
                try (OutputStream os = Files.newOutputStream(Paths.get(fileName))) {
                    os.write(logs.getBytes());
                    logger.info("Логи для контейнера {} записаны в файл: {}", containerName, fileName);
                }
            } else {
                logger.error("Под с именем {} не найдена.", podName);
            }
        } catch (Exception e) {
            logger.error("Ошибка при скачивании логов", e);
        }
        }
    }
//меняеется ли конфиг, будет что-то ещё кроме чтения логов, sout норм?