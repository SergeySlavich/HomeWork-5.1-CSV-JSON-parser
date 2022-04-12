import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){

        String[] data1 = "1,John,Smith,USA,25".split(",");
        String[] data2 = "2,Ivan,Petrov,RU,23".split(",");

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))){
            writer.writeNext(data1);
            writer.writeNext(data2);
        } catch (IOException e){
            e.printStackTrace();
        }

        List<Employee> list = parseCSV(columnMapping, fileName);

        String json = listToJson(list);

        System.out.println(json);
    }
    // Метод parseCSV() вам необходимо реализовать самостоятельно.
    private static List<Employee> parseCSV(String[] columnMapping, String fileName){
        List<Employee> result = new ArrayList<>();
        // В этом вам поможет экземпляр класса CSVReader. Передайте в его конструктор файловый ридер FileReader файла
        // fileName. Данную операцию производите либо в блоке try-catch с ресурсами, либо не забудьте закрыть поток
        // после использования.
        try (CSVReader reader = new CSVReader(new FileReader(fileName))){
            // Так же вам потребуется объект класса ColumnPositionMappingStrategy.
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            // Используя объект стратегии, укажите тип setType() и тип колонок setColumnMapping().
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            // Далее создайте экземпляр CsvToBean с использованием билдера CsvToBeanBuilder.
            // При постройке CsvToBean используйте ранее созданный объект стратегии ColumnPositionMappingStrategy.
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();
            // Созданный экземпляр объекта CsvToBean имеет метод parse(), который вернет вам список сотрудников.
            result = csv.parse();
        } catch (IOException e){
            e.printStackTrace();
        }
        // Полученный список преобразуйте в строчку в формате JSON. Сделайте это с помощью метода listToJson(),
        // который вам так же предстоит реализовать самостоятельно.
        return result;
    }

    private static String listToJson(List<Employee> list) {
        // Для преобразования списка объектов в JSON, требуется определить тип этого списка:
        Type listType = new TypeToken<List<Employee>>() {}.getType();

        // При написании метода listToJson() вам понадобятся объекты типа GsonBuilder и Gson.
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        // Получить JSON из экземпляра класса Gson можно с помощью метода toJson(),
        // передав в качестве аргументов список сотрудников и тип списка:
        String json = gson.toJson(list, listType);

        // Далее запишите полученный JSON в файл с помощью метода writeString(), который необходимо реализовать самостоятельно.
        return writeString(json);
    }

    private static String writeString(String json){
        //JSONObject obj = new JSONObject();
        // Далее запишите полученный JSON в файл с помощью метода writeString(), который необходимо реализовать самостоятельно.
        // В этом вам поможет FileWriter и его метод write().
        try (FileWriter file = new FileWriter("data.json")){
            file.write(json);
            file.flush();
        } catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }
}
