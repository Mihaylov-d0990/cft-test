# Тестовое задание для ЦФТ

- Версия JDK 18;
- Система сборки - Maven 3.8.6;

## Зависимости:
- commons-io 2.11.0 https://mvnrepository.com/artifact/commons-io/commons-io/2.11.0;
- launch4j-maven-plugin 2.1.2 https://mvnrepository.com/artifact/com.akathist.maven.plugins.launch4j/launch4j-maven-plugin/2.1.2.

## Для запуска необходимо:
- Иметь установленный на компьютере JDK 18;
- Иметь установленный на компьютере Maven 3.8.6;
- В корневой папке проекта ввести последовательно "mvn compile" и "mvn package".

Для того чтобы запустить программу необходимо передать следующие параметры: 
тип данных, режим сортировки, имя выходного файла и имена входных файлов.
- Тип данных - обязательный параметр (-i или -s);
- Режим сортировки - необязательный параметр (-a или -d);
- Имя выходного файла - обязательный параметр;
- Имена входных файлов - миниму одно имя файла;

## Примеры запуска программы:
- sort-it.exe -i out.txt in.txt;
- sort-it.exe -s -d out.txt in.txt in1.txt;
- sort-it.exe -s -a out.txt in.txt in1.txt in2.txt;
