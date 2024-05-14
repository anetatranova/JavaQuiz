package cz.cuni.mff.tranova;

import java.util.*;

public class CategoryManager {

    private static Map<String, Integer> categoryToIdMap;
    private static Map<String, Integer> categoryQuestionCount;
    private static Set<String> processedCategories;

    public CategoryManager(){
        this.categoryToIdMap = new LinkedHashMap<>();
        this.categoryQuestionCount = new HashMap<>();
        this.processedCategories = new HashSet<>();
    }

    public static void processCategories(List<Question> questions){
        int index = 1;
        categoryToIdMap.clear();
        categoryQuestionCount.clear();
        processedCategories.clear();
        for (Question q : questions){
            String category = q.getCategory();
            if (!processedCategories.contains(category)) {
                processedCategories.add(category);
                categoryToIdMap.put(category, index++);
                categoryQuestionCount.put(category, 0);
            }
            categoryQuestionCount.put(category, categoryQuestionCount.get(category) + 1);
        }
    }

    public Map<Integer, String> getCategoryIdMap() {
        Map<Integer, String> idToCategoryMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : categoryToIdMap.entrySet()) {
            idToCategoryMap.put(entry.getValue(), entry.getKey());
        }
        return idToCategoryMap;
    }

    public static int getQuestionCountByCategory(String category) {
        return categoryQuestionCount.getOrDefault(category, 0);
    }

}
