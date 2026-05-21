package neu.YYZX.view;

import neu.YYZX.model.CareProject;
import neu.YYZX.model.CareRecord;
import neu.YYZX.model.Elderly;
import neu.YYZX.model.NursingLevel;
import neu.YYZX.model.User;
import neu.YYZX.service.DataManager;
import neu.YYZX.util.FileUtil;

import java.util.ArrayList;
import java.util.Scanner;

public class AdminMenu {
    private final DataManager dm = DataManager.getInstance();
    private final Scanner sc;

    public AdminMenu(Scanner sc) {
        this.sc = sc;
    }

    public void execute() {
        System.out.println("======系统管理员登录======");
        System.out.println("请输入账号：");
        String userName = sc.next();
        System.out.println("请输入密码：");
        String password = sc.next();

        User user = dm.authenticate(userName, password, User.ROLE_ADMIN);
        if (user == null) {
            System.out.println("登录失败！账号、密码或角色不正确");
            return;
        }

        System.out.println("登录成功！欢迎，" + user.getUsername());
        adminLoop();
    }

    private void adminLoop() {
        while (true) {
            System.out.println();
            System.out.println("==========管理员功能菜单==========");
            System.out.println("1---------查询全部护理等级");
            System.out.println("2---------查询全部养护项目");
            System.out.println("3---------养护项目管理(增删改)");
            System.out.println("4---------老人档案管理(增删改查)");
            System.out.println("5---------查询护理执行记录");
            System.out.println("6---------用户账号管理");
            System.out.println("7---------返回主菜单");
            System.out.print("请选择：");

            int choice = readInt();
            switch (choice) {
                case 1:
                    listLevels();
                    break;
                case 2:
                    listProjects();
                    break;
                case 3:
                    projectManageMenu();
                    break;
                case 4:
                    elderManageMenu();
                    break;
                case 5:
                    listRecords();
                    break;
                case 6:
                    userManageMenu();
                    break;
                case 7:
                    return;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void listLevels() {
        ArrayList<NursingLevel> list = dm.getLevels();
        if (list.isEmpty()) {
            System.out.println("无信息，请添加后查询");
            return;
        }
        System.out.println("编码\t等级名称\t巡查频次\t特征描述");
        for (NursingLevel level : list) {
            System.out.printf("%s\t%s\t%s\t%s%n",
                    level.getCode(), level.getName(), level.getFrequency(), level.getDescription());
        }
    }

    private void listProjects() {
        ArrayList<CareProject> list = dm.getProjects();
        if (list.isEmpty()) {
            System.out.println("无信息，请添加后查询");
            return;
        }
        System.out.println("编码\t名称\t类别\t单位\t价格\t周期\t备注");
        for (CareProject p : list) {
            System.out.printf("%s\t%s\t%s\t%s\t%.0f\t%s\t%s%n",
                    p.getCode(), p.getName(), p.getCategory(), p.getUnit(),
                    p.getPrice(), p.getCycle(), p.getRemark());
        }
    }

    private void projectManageMenu() {
        while (true) {
            System.out.println();
            System.out.println("---养护项目管理---");
            System.out.println("1---------添加项目");
            System.out.println("2---------修改项目");
            System.out.println("3---------删除项目");
            System.out.println("4---------返回上级");
            System.out.print("请选择：");
            int choice = readInt();
            switch (choice) {
                case 1:
                    addProject();
                    break;
                case 2:
                    updateProject();
                    break;
                case 3:
                    deleteProject();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void addProject() {
        System.out.print("请输入项目编码：");
        String code = sc.next();
        if (DataManager.containsProject(code)) {
            System.out.println("项目编码已存在，请重新输入");
            return;
        }
        sc.nextLine();
        System.out.print("请输入项目名称：");
        String name = sc.nextLine();
        System.out.print("请输入类别(生活照料/医疗护理/康复心理)：");
        String category = sc.nextLine();
        System.out.print("请输入单位：");
        String unit = sc.next();
        System.out.print("请输入价格：");
        double price = readDouble();
        sc.nextLine();
        System.out.print("请输入执行周期：");
        String cycle = sc.nextLine();
        System.out.print("请输入备注/适用等级：");
        String remark = sc.nextLine();

        dm.getProjects().add(new CareProject(code, name, category, unit, price, cycle, remark));
        FileUtil.persist();
        System.out.println("添加成功！");
    }

    private void updateProject() {
        System.out.print("请输入要修改的项目编码：");
        String code = sc.next();
        int idx = DataManager.getProjectIndex(dm.getProjects(), code);
        if (idx < 0) {
            System.out.println("项目编码不存在，请重新输入");
            return;
        }
        CareProject p = dm.getProjects().get(idx);
        sc.nextLine();
        System.out.print("请输入新项目名称(原:" + p.getName() + ")：");
        String name = sc.nextLine();
        System.out.print("请输入新类别(原:" + p.getCategory() + ")：");
        String category = sc.nextLine();
        System.out.print("请输入新单位(原:" + p.getUnit() + ")：");
        String unit = sc.next();
        System.out.print("请输入新价格(原:" + p.getPrice() + ")：");
        double price = readDouble();
        sc.nextLine();
        System.out.print("请输入新执行周期(原:" + p.getCycle() + ")：");
        String cycle = sc.nextLine();
        System.out.print("请输入新备注(原:" + p.getRemark() + ")：");
        String remark = sc.nextLine();

        p.setName(name);
        p.setCategory(category);
        p.setUnit(unit);
        p.setPrice(price);
        p.setCycle(cycle);
        p.setRemark(remark);
        FileUtil.persist();
        System.out.println("修改成功！");
    }

    private void deleteProject() {
        System.out.print("请输入要删除的项目编码：");
        String code = sc.next();
        int idx = DataManager.getProjectIndex(dm.getProjects(), code);
        if (idx < 0) {
            System.out.println("项目编码不存在，请重新输入");
            return;
        }
        dm.getProjects().remove(idx);
        FileUtil.persist();
        System.out.println("删除成功！");
    }

    private void elderManageMenu() {
        while (true) {
            System.out.println();
            System.out.println("---老人档案管理---");
            System.out.println("1---------查询全部老人");
            System.out.println("2---------添加老人");
            System.out.println("3---------修改老人");
            System.out.println("4---------删除老人");
            System.out.println("5---------返回上级");
            System.out.print("请选择：");
            int choice = readInt();
            switch (choice) {
                case 1:
                    listElders();
                    break;
                case 2:
                    addElder();
                    break;
                case 3:
                    updateElder();
                    break;
                case 4:
                    deleteElder();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void listElders() {
        ArrayList<Elderly> list = dm.getElders();
        if (list.isEmpty()) {
            System.out.println("无信息，请添加后查询");
            return;
        }
        System.out.println("编号\t姓名\t年龄\t性别\t护理等级\t房间号");
        for (Elderly e : list) {
            System.out.printf("%s\t%s\t%d\t%s\t%s\t%s%n",
                    e.getId(), e.getName(), e.getAge(), e.getGender(),
                    e.getNursingLevelCode(), e.getRoomNo());
        }
    }

    private void addElder() {
        System.out.print("请输入老人编号：");
        String id = sc.next();
        if (DataManager.containsElder(id)) {
            System.out.println("老人编号已存在，请重新输入");
            return;
        }
        sc.nextLine();
        System.out.print("请输入姓名：");
        String name = sc.nextLine();
        System.out.print("请输入年龄：");
        int age = readInt();
        System.out.print("请输入性别：");
        String gender = sc.next();
        System.out.print("请输入护理等级编码(ZL/HL-1/HL-2/HL-3/YZ)：");
        String levelCode = sc.next();
        if (!DataManager.containsLevel(levelCode)) {
            System.out.println("护理等级不存在，请重新输入");
            return;
        }
        sc.nextLine();
        System.out.print("请输入房间号：");
        String roomNo = sc.nextLine();

        dm.getElders().add(new Elderly(id, name, age, gender, levelCode, roomNo));
        FileUtil.persist();
        System.out.println("添加成功！");
    }

    private void updateElder() {
        System.out.print("请输入要修改的老人编号：");
        String id = sc.next();
        int idx = DataManager.getElderIndex(dm.getElders(), id);
        if (idx < 0) {
            System.out.println("老人编号不存在，请重新输入");
            return;
        }
        Elderly e = dm.getElders().get(idx);
        sc.nextLine();
        System.out.print("请输入新姓名(原:" + e.getName() + ")：");
        String name = sc.nextLine();
        System.out.print("请输入新年龄(原:" + e.getAge() + ")：");
        int age = readInt();
        System.out.print("请输入新性别(原:" + e.getGender() + ")：");
        String gender = sc.next();
        System.out.print("请输入新护理等级(原:" + e.getNursingLevelCode() + ")：");
        String levelCode = sc.next();
        if (!DataManager.containsLevel(levelCode)) {
            System.out.println("护理等级不存在，请重新输入");
            return;
        }
        sc.nextLine();
        System.out.print("请输入新房间号(原:" + e.getRoomNo() + ")：");
        String roomNo = sc.nextLine();

        e.setName(name);
        e.setAge(age);
        e.setGender(gender);
        e.setNursingLevelCode(levelCode);
        e.setRoomNo(roomNo);
        FileUtil.persist();
        System.out.println("修改成功！");
    }

    private void deleteElder() {
        System.out.print("请输入要删除的老人编号：");
        String id = sc.next();
        int idx = DataManager.getElderIndex(dm.getElders(), id);
        if (idx < 0) {
            System.out.println("老人编号不存在，请重新输入");
            return;
        }
        dm.getElders().remove(idx);
        FileUtil.persist();
        System.out.println("删除成功！");
    }

    private void listRecords() {
        ArrayList<CareRecord> list = dm.getRecords();
        if (list.isEmpty()) {
            System.out.println("无信息，请添加后查询");
            return;
        }
        System.out.println("记录号\t老人编号\t项目编码\t执行时间\t护工\t备注");
        for (CareRecord r : list) {
            System.out.printf("%s\t%s\t%s\t%s\t%s\t%s%n",
                    r.getId(), r.getElderlyId(), r.getProjectCode(),
                    r.getExecuteTime(), r.getNurseName(),
                    r.getRemark() == null ? "" : r.getRemark());
        }
    }

    private void userManageMenu() {
        while (true) {
            System.out.println();
            System.out.println("---用户账号管理---");
            System.out.println("1---------查询全部用户");
            System.out.println("2---------添加用户");
            System.out.println("3---------修改用户密码");
            System.out.println("4---------删除用户");
            System.out.println("5---------返回上级");
            System.out.print("请选择：");
            int choice = readInt();
            switch (choice) {
                case 1:
                    listUsers();
                    break;
                case 2:
                    addUser();
                    break;
                case 3:
                    updateUserPassword();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("输入有误，请重新输入");
            }
        }
    }

    private void listUsers() {
        ArrayList<User> list = dm.getUsers();
        if (list.isEmpty()) {
            System.out.println("无信息，请添加后查询");
            return;
        }
        System.out.println("账号\t角色");
        for (User user : list) {
            String roleName = User.ROLE_ADMIN.equalsIgnoreCase(user.getRole()) ? "管理员" : "护工";
            System.out.printf("%s\t%s%n", user.getUsername(), roleName);
        }
    }

    private void addUser() {
        System.out.print("请输入账号：");
        String username = sc.next();
        if (DataManager.containsUser(username)) {
            System.out.println("账号已存在，请重新输入");
            return;
        }
        System.out.print("请输入密码：");
        String password = sc.next();
        System.out.print("请输入角色(admin/nurse)：");
        String role = sc.next().toLowerCase();
        if (!User.ROLE_ADMIN.equals(role) && !User.ROLE_NURSE.equals(role)) {
            System.out.println("角色无效，请输入 admin 或 nurse");
            return;
        }
        dm.getUsers().add(new User(username, password, role));
        FileUtil.persist();
        System.out.println("用户添加成功！");
    }

    private void updateUserPassword() {
        System.out.print("请输入要修改的账号：");
        String username = sc.next();
        int idx = DataManager.getUserIndex(dm.getUsers(), username);
        if (idx < 0) {
            System.out.println("账号不存在，请重新输入");
            return;
        }
        System.out.print("请输入新密码：");
        String password = sc.next();
        dm.getUsers().get(idx).setPassword(password);
        FileUtil.persist();
        System.out.println("密码修改成功！");
    }

    private void deleteUser() {
        System.out.print("请输入要删除的账号：");
        String username = sc.next();
        int idx = DataManager.getUserIndex(dm.getUsers(), username);
        if (idx < 0) {
            System.out.println("账号不存在，请重新输入");
            return;
        }
        User user = dm.getUsers().get(idx);
        if (User.ROLE_ADMIN.equalsIgnoreCase(user.getRole()) && dm.countAdmins() <= 1) {
            System.out.println("至少保留一名管理员，无法删除");
            return;
        }
        dm.getUsers().remove(idx);
        FileUtil.persist();
        System.out.println("用户删除成功！");
    }

    private int readInt() {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("请输入数字：");
        }
        int value = sc.nextInt();
        return value;
    }

    private double readDouble() {
        while (!sc.hasNextDouble()) {
            sc.next();
            System.out.print("请输入数字：");
        }
        return sc.nextDouble();
    }
}
