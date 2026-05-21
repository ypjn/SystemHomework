package neu.YYZX.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统全部数据的 JSON 持久化载体（Jackson 序列化用）
 */
public class SystemData {
    private List<NursingLevel> levels = new ArrayList<>();
    private List<CareProject> projects = new ArrayList<>();
    private List<Elderly> elders = new ArrayList<>();
    private List<CareRecord> records = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private int recordSeq = 1;

    public SystemData() {
    }

    public List<NursingLevel> getLevels() {
        return levels;
    }

    public void setLevels(List<NursingLevel> levels) {
        this.levels = levels;
    }

    public List<CareProject> getProjects() {
        return projects;
    }

    public void setProjects(List<CareProject> projects) {
        this.projects = projects;
    }

    public List<Elderly> getElders() {
        return elders;
    }

    public void setElders(List<Elderly> elders) {
        this.elders = elders;
    }

    public List<CareRecord> getRecords() {
        return records;
    }

    public void setRecords(List<CareRecord> records) {
        this.records = records;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getRecordSeq() {
        return recordSeq;
    }

    public void setRecordSeq(int recordSeq) {
        this.recordSeq = recordSeq;
    }
}
