package first_task;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Date;


interface IFace{
    String path = "D:\\JavaProjectsCommunity\\Lab_6\\currentdata.txt";
    File file = new File(path);
}
//________________________________________________

class FirstThread extends Thread
{
    class MyDate  implements IFace
    {
        private Date date;

        public void getDate_and_writeToFile()
        {
            try {
                date = new Date();
                String dateTime = date.toString();
                System.out.println(dateTime);

                PrintWriter pw = new PrintWriter(new FileWriter(path, true));
                pw.println(dateTime);
                pw.close();
            }
            catch (IOException e){e.printStackTrace();}
        }

    }
    public void run(){
        System.out.println("Запустился 1-й поток");
        MyDate link1 = new MyDate();

        while(!Thread.interrupted())
        {
            try {
                Thread.sleep(1 * 1000);
                link1.getDate_and_writeToFile();
            } catch (InterruptedException e) {
                //System.out.println("1-й поток завершён");
                //return;
            }
        }
    }
}
//_______________________________________________________________
class SecondThread extends Thread{

    class CheckSize implements IFace
    {
        private File newFile;
        private String destinationPath = "E:\\JavaProjects\\Lab_6\\";

        public long getFileSize()
        {
            return  this.file.length();
        }

        public void saveInNewFile_and_clearPastFile() throws IOException {

            this.newFile = new File(destinationPath+"newFile.txt");
            newFile.createNewFile();

            String newFileName = getNewNameOfFile();
            newFile.renameTo(new File(newFileName));
            System.out.println("saveInNewFile_and_clearPastFile(): новый файл создан и переименован");

            newFile = new File(destinationPath+newFileName);
            FileChannel sourceChannel = new FileInputStream(file).getChannel();
            FileChannel destChannel = new FileOutputStream(newFile).getChannel();
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            sourceChannel.close();
            destChannel.close();


            new FileOutputStream(this.file, false).close(); //чистка первого файла
            System.out.println("saveInNewFile_and_clearPastFile(): старый файл отчищен");
        }

        private String getNewNameOfFile()
        {
            long timestamp = newFile.lastModified();
            Date dateOfCreation = new Date(timestamp);
            StringBuilder creationTime = new StringBuilder(dateOfCreation.toString());
            creationTime.replace(13,14,"h");
            creationTime.replace(16,17,"m");
            creationTime.replace(19,20,"s ");
            creationTime.append(".txt");

            return new String(creationTime);
        }
    }

    public void run(){
        System.out.println("Запустился 2-й поток");
        CheckSize link2 = new CheckSize();

        while(!Thread.interrupted())
        {
            try {
                Thread.sleep(5 * 1000);
            }
            catch (InterruptedException e){}

            if (link2.getFileSize() > 50)
            {
                System.out.println("Размер превышен");
                try {
                    link2.saveInNewFile_and_clearPastFile();
                }
                catch (IOException e){e.printStackTrace();}
                //System.out.println("2-й поток завершён");

                //return;
            }
        }
    }
}


public class Main {
    public static void main(String[] args) {
        System.out.println("Main");

        FirstThread firstThread = new FirstThread();
        SecondThread secondThread = new SecondThread();
        secondThread.start();
        firstThread.start();

//        try{
////            secondThread.join();
////            firstThread.interrupt();
//        }
//        catch (InterruptedException e){
//            e.printStackTrace();
//        }

    }
}
/*
Напишите программу, запускающую 2 независимых потока. Первый поток выводит на экран дату и время
каждые 5 секунд и это время записывает в файл currentdata.txt. Для определения даты и времени
использовать класс java.util.Calendar. Второй поток каждые 15 секунд проверяет размер файла
currentdata.txt и если этот размер превысил 50 байт, то сохраняет файл под уникальным именем и
обнуляет файл currentdata.txt. Уникальное имя файла должно содержать дату и время его создания.
Программа должна содержать 3 класса: первый реализует первый поток, второй класс реализует второй
поток и третий класс содержит метод main, из которого запускаются оба потока.

Создайте документацию по проекту в формате html с помощью javaDoc.

*/
