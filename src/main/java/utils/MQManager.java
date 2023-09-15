package utils;

import com.ibm.mq.*;
import com.ibm.mq.constants.MQConstants;
import com.ibm.mq.headers.MQDataException;
import com.ibm.mq.headers.pcf.PCFMessage;
import com.ibm.mq.headers.pcf.PCFMessageAgent;
import config.MQConfig;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MQManager {
    private MQConfig mqConfig;

    public MQManager(String host, int port, String channel, String manager, String user, String password) {
        this.mqConfig = new MQConfig(host, port, channel, manager, user, password);
        mqConfig.configureConnection();
    }

    /**
     * Отправляет сообщение в указанную очередь IBM MQ.
     * @param queueName Имя целевой очереди.
     * @param message Сообщение для отправки.
     */
    public void sendMessage(String queueName, String message) {
        try {
            // Создаем объект MQQueueManager для управления соединением с MQ Manager.
            MQQueueManager queueManager = new MQQueueManager(mqConfig.getManager());

            // Устанавливаем опции для открытия очереди (в данном случае, для вывода сообщений).
            int openOptions = MQConstants.MQOO_OUTPUT + MQConstants.MQOO_FAIL_IF_QUIESCING;

            // Открываем очередь для вывода сообщений.
            MQQueue queue = queueManager.accessQueue(queueName, openOptions);

            // Создаем объект MQMessage для представления сообщения.
            MQMessage mqMessage = new MQMessage();

            // Записываем текст сообщения.
            mqMessage.writeString(message);

            // Устанавливаем опции для помещения сообщения в очередь.
            MQPutMessageOptions pmo = new MQPutMessageOptions();

            // Помещаем сообщение в очередь.
            queue.put(mqMessage, pmo);

            // Закрываем очередь.
            queue.close();

            // Отключаемся от MQ Manager.
            queueManager.disconnect();
        } catch (MQException | IOException e) {
            // В случае ошибки MQException, печатаем трассировку стека.
            e.printStackTrace();
        }
    }

    /**
     * Читает сообщение из указанной очереди IBM MQ.
     * @param queueName Имя целевой очереди.
     * @return Прочитанное сообщение или null, если очередь пуста.
     */
    public String readMessage(String queueName) {
        try {
            // Создаем объект MQQueueManager для управления соединением с MQ Manager.
            MQQueueManager queueManager = new MQQueueManager(mqConfig.getManager());

            // Устанавливаем опции для открытия очереди (в данном случае, для чтения сообщений).
            int openOptions = MQConstants.MQOO_INPUT_SHARED + MQConstants.MQOO_FAIL_IF_QUIESCING;

            // Открываем очередь для чтения сообщений.
            MQQueue queue = queueManager.accessQueue(queueName, openOptions);

            // Устанавливаем опции для операции чтения (ждать, если очередь пуста и не ждать, если менеджер завершается).
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.options = MQConstants.MQGMO_WAIT | MQConstants.MQGMO_FAIL_IF_QUIESCING;
            gmo.waitInterval = 5000; // Время ожидания в миллисекундах (здесь 5 секунд)

            // Создаем объект MQMessage для представления сообщения.
            MQMessage mqMessage = new MQMessage();

            // Чтение сообщения из очереди.
            queue.get(mqMessage, gmo);

            // Преобразование считанного сообщения в строку.
            String message = mqMessage.readStringOfByteLength(mqMessage.getMessageLength());

            // Закрытие очереди и отключение от MQ Manager.
            queue.close();
            queueManager.disconnect();

            // Возвращаем прочитанное сообщение.
            return message;
        } catch (MQException e) {
            if (e.reasonCode == MQConstants.MQRC_NO_MSG_AVAILABLE) {
                // Очередь пуста, нет доступных сообщений, возвращаем null.
                return null;
            } else {
                // В случае другой MQException, печатаем трассировку стека и возвращаем null.
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            // В случае ошибки ввода/вывода, печатаем трассировку стека и возвращаем null.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод для получения списка локальных очередей MQ.
     *
     * @return Список имен локальных очередей.
     * @throws MQException Если произошла ошибка взаимодействия с MQ Manager.
     * @throws IOException Если произошла ошибка ввода/вывода.
     */
    public List<String> getQueueList() throws MQException, IOException, MQDataException {
        List<String> queueList = new ArrayList<>();

        MQQueueManager queueManager = new MQQueueManager(mqConfig.getManager());

        // Создание агента для отправки PCF-запросов
        PCFMessageAgent agent = new PCFMessageAgent(queueManager);

        // Создание запроса для получения списка локальных очередей
        PCFMessage request = new PCFMessage(MQConstants.MQCMD_INQUIRE_Q);

        // Установка параметров запроса
        request.addParameter(MQConstants.MQIA_Q_TYPE, MQConstants.MQQT_LOCAL);
        request.addParameter(MQConstants.MQCA_Q_NAME, "*");

        // Отправка запроса и получение ответа
        PCFMessage[] responses = agent.send(request);

        // Обработка ответа
        for (PCFMessage response : responses) {
            String queueName = response.getStringParameterValue(MQConstants.MQCA_Q_NAME);
            if (queueName != null) {
                queueList.add(queueName);
            }
        }

        return queueList;
    }

    /**
     * Закрывает соединение с IBM MQ.
     * @param connection Соединение с IBM MQ.
     * @param session    Сессия с IBM MQ.
     */
    public void closeConnection(Connection connection, Session session) {
        try {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    /**
     * Обрабатывает ошибки и исключения при работе с IBM MQ.
     *
     * @param e Исключение, которое нужно обработать.
     */
    public void handleErrors(Exception e) {
        if (e instanceof JMSException) {
            // Обработка ошибок JMS
            JMSException jmsException = (JMSException) e;
            // Дополнительная обработка или логирование
            jmsException.printStackTrace();
        } else if (e instanceof MQException) {
            // Обработка ошибок MQ
            MQException mqException = (MQException) e;
            // Дополнительная обработка или логирование
            mqException.printStackTrace();
        } else {
            // Другие типы исключений
            // Обработка или логирование по усмотрению
            e.printStackTrace();
        }
    }


}

