package com.ohgiraffers.run;

import com.ohgiraffers.model.CategoryDTO;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Stream;

import static com.ohgiraffers.common.JDBCTemplate.close;
import static com.ohgiraffers.common.JDBCTemplate.getConnection;

public class MainMenu {
    //region
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";

    public Connection con = getConnection();
    public Properties prop = new Properties();
    public PreparedStatement pState = null;
    public ResultSet rSet = null;
    public int result = 0;
//endregion
    public MainMenu() {
        Scanner sc = new Scanner(System.in);
        try {
            prop.loadFromXML(new FileInputStream("src/main/java/com/ohgiraffers/mapper/category-query.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int selectedNumber;
        mainmenu:
        while (true) {
            selectedNumber = 99;
            result = 0;
            System.out.println("=====================================");
            System.out.println("         chap03-crud-practice         ");
            System.out.println("=====================================");
            System.out.println(BLUE + "1. Search for Category Code" + RESET);
            System.out.println(RED + "2. Search for Ref Category Code" + RESET);
            System.out.println("3. Insert Category");
            System.out.println("4. Update Category");
            System.out.println("5. Delete Category");
            System.out.println("0. Program Exit");
            System.out.print(GREEN + "PLZ enter a number : " + RESET);
            selectedNumber = sc.nextInt();
            sc.nextLine();

            switch (selectedNumber) {
                case 1 -> {
                    //카테고리 코드로 검색 (1행)
                    System.out.println(BLUE + "=====================================" + RESET);
                    System.out.print("Category Code...? : ");
                    int code = sc.nextInt();
                    search(code, prop.getProperty("findAtCategoryCode"));
                }
                case 2 -> {
                    //상위 카테고리 코드로 검색 (n행)
                    System.out.println(BLUE + "=====================================" + RESET);
                    System.out.print("Ref Category Code...? : ");
                    int code = sc.nextInt();
                    search(code, prop.getProperty("findAtRefCategoryCode"));
                }
                case 3 -> {
                    //카테고리 입력
                    System.out.println(BLUE + "=====================================" + RESET);
                    System.out.println("Create new Category");
                    System.out.print("Category name...? :");
                    String name = sc.nextLine();
                    System.out.println("[ 식사(1) / 음료(2) / 디저트(3) / default : 1");
                    System.out.print("Ref category code...? : ");
                    int refCode = sc.nextInt();

                    insert(createDTO(name, refCode), prop.getProperty("insertCategory"));
                }
                case 4 -> {
                    //카테고리 수정
                    System.out.println(BLUE + "=====================================" + RESET);
                    System.out.println("Update Category");
                    System.out.print("Category Code...? : ");
                    int target = sc.nextInt();
                    sc.nextLine();
                    System.out.print("Category name...? :");
                    String name = sc.nextLine();
                    System.out.println("[ 식사(1) / 음료(2) / 디저트(3) / default : 1");
                    System.out.print("Ref category code...? : ");
                    int refCode = sc.nextInt();
                    update(createDTO(name, refCode), target, prop.getProperty("updateCategory"));

                }
                case 5 -> {
                    //카테고리 삭제
                    System.out.println(BLUE + "=====================================" + RESET);
                    System.out.println("Delete Category");
                    System.out.print("Category Code...? : ");
                    int target = sc.nextInt();
                    delete(target,prop.getProperty("deleteCategory"));
                }
                case 0 -> {
                    System.out.println(RED + "Program finished..." + RESET);
                    break mainmenu;
                }
                default -> {
                    System.out.println("Wrong number. Try again...");
                }
            }
        }
        close(con);
    }
    public void search(int code, String query) {
        List<CategoryDTO> dtos = new ArrayList<>();

        try {
            pState = con.prepareStatement(query);
            pState.setInt(1, code);
            rSet = pState.executeQuery();
            while (rSet.next()) {
                dtos.add(new CategoryDTO(rSet.getInt("category_code"), rSet.getString("category_name"), rSet.getInt("ref_category_code")));
            }
            Stream<CategoryDTO> stream = dtos.stream();

            System.out.println(GREEN + "=====================================" + RESET);
            System.out.println("<<<code, name, Ref_code>>>");
            stream.forEach(System.out::println);
            System.out.println(GREEN + "=====================================" + RESET);
            System.out.println("Enter any KEY to next...");
            pause();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //close(con);
            close(pState);
            close(rSet);
        }
    }

    public void insert(CategoryDTO newDTO, String query) {
        try {
            pState = con.prepareStatement(query);
            pState.setString(1, newDTO.getCategoryName());
            pState.setInt(2, newDTO.getRefCategoryCode());

            result = pState.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // close(con)
            close(pState);
        }
        if (result > 0) {
            System.out.println("(" + newDTO + ") Insert complete...");
        } else System.out.println("Insert failed...");
    }

    public void update(CategoryDTO updateDTO, int targetId, String query) {
        try {
            pState = con.prepareStatement(query);
            pState.setString(1, updateDTO.getCategoryName());
            pState.setInt(2, updateDTO.getRefCategoryCode());
            pState.setInt(3, targetId);

            result = pState.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(pState);
        }
        if (result > 0) {
            System.out.println("(" + updateDTO + " To " + targetId + " ) Update complete...");
        } else System.out.println("Update failed...");
    }

    public void delete(int targetId, String query) {
        try {
            pState = con.prepareStatement(query);
            pState.setInt(1, targetId);

            result = pState.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (result > 0) {
            System.out.println("(" + targetId + " ) Delete complete...");
        } else System.out.println("Delete failed...");
    }

    public CategoryDTO createDTO(String name, int refCode) {
        if (refCode < 1 || refCode > 3) {
            refCode = 1;
        }
        CategoryDTO newDTO = new CategoryDTO();
        newDTO.setCategoryName(name);
        newDTO.setRefCategoryCode(refCode);
        return newDTO;
    }

    public static void pause() {
        try {
            System.in.read();
        } catch (IOException e) {
        }
    }
}
