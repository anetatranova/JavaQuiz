package cz.cuni.mff.tranova;

import java.util.*;

/**
 * Spravuje katergorie kvízu - mapuje ID a kategorie, přiřazuje otázky k vhodné kategorii, počítá otzáky
 */
public class CategoryManager {

    private static Map<String, Integer> categoryToIdMap;
    private static Map<String, Integer> categoryQuestionCount;
    private static Set<String> processedCategories;
    /**
     * Kontruktory pro novou CategoryManager instanci
     */
    public CategoryManager(){
        this.categoryToIdMap = new LinkedHashMap<>();
        this.categoryQuestionCount = new HashMap<>();
        this.processedCategories = new HashSet<>();
    }

    /**
     * Zpracovává seznam otázek - přiřadí je ke kategoriím a spočítá jejich počet v každé kategorii
     *
     * @param questions seznam otázek ke zpracování
     */
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
    /**
     * Získá mapovaní ID a kategorií
     *
     * @return mapování ID a kategorie
     */
    public Map<Integer, String> getCategoryIdMap() {
        Map<Integer, String> idToCategoryMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : categoryToIdMap.entrySet()) {
            idToCategoryMap.put(entry.getValue(), entry.getKey());
        }
        return idToCategoryMap;
    }
    /**
     * Získá počet otázek v kategorii
     *
     * @param category jméno kategorie
     * @return počet otázek v dané kategorii
     */

    public static int getQuestionCountByCategory(String category) {
        return categoryQuestionCount.getOrDefault(category, 0);
    }

}
