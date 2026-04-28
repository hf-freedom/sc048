package com.pet.hospital.store;

import com.pet.hospital.model.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class DataStore {

    private final AtomicLong idCounter = new AtomicLong(1);

    private Map<Long, Department> departments = new ConcurrentHashMap<>();
    private Map<Long, Doctor> doctors = new ConcurrentHashMap<>();
    private Map<Long, Owner> owners = new ConcurrentHashMap<>();
    private Map<Long, Pet> pets = new ConcurrentHashMap<>();
    private Map<Long, Medicine> medicines = new ConcurrentHashMap<>();
    private Map<Long, MedicineBatch> medicineBatches = new ConcurrentHashMap<>();
    private Map<Long, Consumable> consumables = new ConcurrentHashMap<>();
    private Map<Long, TreatmentItem> treatmentItems = new ConcurrentHashMap<>();
    private Map<Long, Schedule> schedules = new ConcurrentHashMap<>();
    private Map<Long, Cage> cages = new ConcurrentHashMap<>();
    private Map<Long, Consultation> consultations = new ConcurrentHashMap<>();
    private Map<Long, Prescription> prescriptions = new ConcurrentHashMap<>();
    private Map<Long, Boarding> boardings = new ConcurrentHashMap<>();
    private Map<Long, Bill> bills = new ConcurrentHashMap<>();
    private Map<Long, Reminder> reminders = new ConcurrentHashMap<>();

    public Long generateId() {
        return idCounter.getAndIncrement();
    }

    public String generateCode(String prefix) {
        return prefix + String.format("%06d", idCounter.get());
    }

    @PostConstruct
    public void initData() {
        initDepartments();
        initDoctors();
        initMedicines();
        initMedicineBatches();
        initConsumables();
        initTreatmentItems();
        initCages();
        initOwnersAndPets();
        initSchedules();
    }

    private void initDepartments() {
        Department dept1 = new Department();
        dept1.setId(generateId());
        dept1.setCode("D001");
        dept1.setName("内科");
        dept1.setDescription("宠物内科疾病诊疗");
        departments.put(dept1.getId(), dept1);

        Department dept2 = new Department();
        dept2.setId(generateId());
        dept2.setCode("D002");
        dept2.setName("外科");
        dept2.setDescription("宠物外科手术治疗");
        departments.put(dept2.getId(), dept2);

        Department dept3 = new Department();
        dept3.setId(generateId());
        dept3.setCode("D003");
        dept3.setName("疫苗科");
        dept3.setDescription("宠物疫苗接种");
        departments.put(dept3.getId(), dept3);

        Department dept4 = new Department();
        dept4.setId(generateId());
        dept4.setCode("D004");
        dept4.setName("皮肤科");
        dept4.setDescription("宠物皮肤疾病诊疗");
        departments.put(dept4.getId(), dept4);
    }

    private void initDoctors() {
        Doctor doc1 = new Doctor();
        doc1.setId(generateId());
        doc1.setName("张医生");
        doc1.setTitle("主任医师");
        doc1.setPhone("13800138001");
        doc1.setDepartmentId(1L);
        doc1.setDepartmentName("内科");
        doc1.setAuthorizedTreatmentIds(Arrays.asList(1L, 2L));
        doctors.put(doc1.getId(), doc1);

        Doctor doc2 = new Doctor();
        doc2.setId(generateId());
        doc2.setName("李医生");
        doc2.setTitle("副主任医师");
        doc2.setPhone("13800138002");
        doc2.setDepartmentId(2L);
        doc2.setDepartmentName("外科");
        doc2.setAuthorizedTreatmentIds(Arrays.asList(3L, 4L));
        doctors.put(doc2.getId(), doc2);

        Doctor doc3 = new Doctor();
        doc3.setId(generateId());
        doc3.setName("王医生");
        doc3.setTitle("主治医师");
        doc3.setPhone("13800138003");
        doc3.setDepartmentId(3L);
        doc3.setDepartmentName("疫苗科");
        doc3.setAuthorizedTreatmentIds(Arrays.asList(5L, 6L, 7L));
        doctors.put(doc3.getId(), doc3);
    }

    private void initMedicines() {
        Medicine med1 = new Medicine();
        med1.setId(generateId());
        med1.setCode("M001");
        med1.setName("阿莫西林");
        med1.setSpecification("500mg/片");
        med1.setUnit("片");
        med1.setSalePrice(new BigDecimal("2.50"));
        med1.setCostPrice(new BigDecimal("0.80"));
        med1.setCategory("抗生素");
        medicines.put(med1.getId(), med1);

        Medicine med2 = new Medicine();
        med2.setId(generateId());
        med2.setCode("M002");
        med2.setName("头孢拉定");
        med2.setSpecification("250mg/粒");
        med2.setUnit("粒");
        med2.setSalePrice(new BigDecimal("3.00"));
        med2.setCostPrice(new BigDecimal("1.00"));
        med2.setCategory("抗生素");
        medicines.put(med2.getId(), med2);

        Medicine med3 = new Medicine();
        med3.setId(generateId());
        med3.setCode("M003");
        med3.setName("布洛芬");
        med3.setSpecification("100mg/片");
        med3.setUnit("片");
        med3.setSalePrice(new BigDecimal("1.50"));
        med3.setCostPrice(new BigDecimal("0.50"));
        med3.setCategory("止痛药");
        medicines.put(med3.getId(), med3);

        Medicine med4 = new Medicine();
        med4.setId(generateId());
        med4.setCode("M004");
        med4.setName("驱虫药");
        med4.setSpecification("1片/盒");
        med4.setUnit("盒");
        med4.setSalePrice(new BigDecimal("30.00"));
        med4.setCostPrice(new BigDecimal("10.00"));
        med4.setCategory("驱虫药");
        medicines.put(med4.getId(), med4);
    }

    private void initMedicineBatches() {
        MedicineBatch batch1 = new MedicineBatch();
        batch1.setId(generateId());
        batch1.setMedicineId(1L);
        batch1.setMedicineName("阿莫西林");
        batch1.setBatchNumber("B2024001");
        batch1.setProductionDate(LocalDate.of(2024, 1, 1));
        batch1.setExpiryDate(LocalDate.of(2025, 12, 31));
        batch1.setQuantity(1000);
        batch1.setAvailableQuantity(1000);
        batch1.setPurchasePrice(new BigDecimal("0.80"));
        batch1.setSupplier("正规医药公司");
        medicineBatches.put(batch1.getId(), batch1);

        MedicineBatch batch2 = new MedicineBatch();
        batch2.setId(generateId());
        batch2.setMedicineId(1L);
        batch2.setMedicineName("阿莫西林");
        batch2.setBatchNumber("B2024002");
        batch2.setProductionDate(LocalDate.of(2024, 6, 1));
        batch2.setExpiryDate(LocalDate.of(2026, 5, 31));
        batch2.setQuantity(2000);
        batch2.setAvailableQuantity(2000);
        batch2.setPurchasePrice(new BigDecimal("0.75"));
        batch2.setSupplier("正规医药公司");
        medicineBatches.put(batch2.getId(), batch2);

        MedicineBatch batch3 = new MedicineBatch();
        batch3.setId(generateId());
        batch3.setMedicineId(2L);
        batch3.setMedicineName("头孢拉定");
        batch3.setBatchNumber("B2024003");
        batch3.setProductionDate(LocalDate.of(2024, 2, 1));
        batch3.setExpiryDate(LocalDate.of(2026, 1, 31));
        batch3.setQuantity(500);
        batch3.setAvailableQuantity(500);
        batch3.setPurchasePrice(new BigDecimal("1.00"));
        batch3.setSupplier("正规医药公司");
        medicineBatches.put(batch3.getId(), batch3);
    }

    private void initConsumables() {
        Consumable con1 = new Consumable();
        con1.setId(generateId());
        con1.setCode("C001");
        con1.setName("一次性注射器");
        con1.setSpecification("5ml");
        con1.setUnit("支");
        con1.setSalePrice(new BigDecimal("1.00"));
        con1.setCostPrice(new BigDecimal("0.30"));
        con1.setStock(500);
        con1.setCategory("注射用品");
        consumables.put(con1.getId(), con1);

        Consumable con2 = new Consumable();
        con2.setId(generateId());
        con2.setCode("C002");
        con2.setName("手术刀片");
        con2.setSpecification("11号");
        con2.setUnit("个");
        con2.setSalePrice(new BigDecimal("5.00"));
        con2.setCostPrice(new BigDecimal("2.00"));
        con2.setStock(100);
        con2.setCategory("手术用品");
        consumables.put(con2.getId(), con2);

        Consumable con3 = new Consumable();
        con3.setId(generateId());
        con3.setCode("C003");
        con3.setName("医用棉花");
        con3.setSpecification("50g/包");
        con3.setUnit("包");
        con3.setSalePrice(new BigDecimal("8.00"));
        con3.setCostPrice(new BigDecimal("3.00"));
        con3.setStock(200);
        con3.setCategory("护理用品");
        consumables.put(con3.getId(), con3);
    }

    private void initTreatmentItems() {
        TreatmentItem item1 = new TreatmentItem();
        item1.setId(generateId());
        item1.setCode("T001");
        item1.setName("常规体检");
        item1.setDepartmentId(1L);
        item1.setDepartmentName("内科");
        item1.setPrice(new BigDecimal("100.00"));
        item1.setCost(new BigDecimal("20.00"));
        item1.setNeedFollowUp(false);
        item1.setDescription("宠物常规身体检查");
        treatmentItems.put(item1.getId(), item1);

        TreatmentItem item2 = new TreatmentItem();
        item2.setId(generateId());
        item2.setCode("T002");
        item2.setName("感冒治疗");
        item2.setDepartmentId(1L);
        item2.setDepartmentName("内科");
        item2.setPrice(new BigDecimal("150.00"));
        item2.setCost(new BigDecimal("50.00"));
        item2.setNeedFollowUp(true);
        item2.setFollowUpDays(7);
        item2.setDescription("宠物感冒诊疗");
        treatmentItems.put(item2.getId(), item2);

        TreatmentItem item3 = new TreatmentItem();
        item3.setId(generateId());
        item3.setCode("T003");
        item3.setName("外科手术");
        item3.setDepartmentId(2L);
        item3.setDepartmentName("外科");
        item3.setPrice(new BigDecimal("500.00"));
        item3.setCost(new BigDecimal("200.00"));
        item3.setRequiredConsumableIds(Arrays.asList(2L, 3L));
        item3.setNeedFollowUp(true);
        item3.setFollowUpDays(14);
        item3.setDescription("宠物外科手术治疗");
        treatmentItems.put(item3.getId(), item3);

        TreatmentItem item4 = new TreatmentItem();
        item4.setId(generateId());
        item4.setCode("T004");
        item4.setName("伤口处理");
        item4.setDepartmentId(2L);
        item4.setDepartmentName("外科");
        item4.setPrice(new BigDecimal("80.00"));
        item4.setCost(new BigDecimal("20.00"));
        item4.setRequiredConsumableIds(Arrays.asList(3L));
        item4.setNeedFollowUp(false);
        item4.setDescription("宠物外伤伤口处理");
        treatmentItems.put(item4.getId(), item4);

        TreatmentItem item5 = new TreatmentItem();
        item5.setId(generateId());
        item5.setCode("T005");
        item5.setName("犬狂犬疫苗");
        item5.setDepartmentId(3L);
        item5.setDepartmentName("疫苗科");
        item5.setPrice(new BigDecimal("80.00"));
        item5.setCost(new BigDecimal("30.00"));
        item5.setRequiredConsumableIds(Arrays.asList(1L));
        item5.setIsVaccine(true);
        item5.setNextVaccineDays(365);
        item5.setNeedFollowUp(false);
        item5.setDescription("犬狂犬疫苗接种");
        treatmentItems.put(item5.getId(), item5);

        TreatmentItem item6 = new TreatmentItem();
        item6.setId(generateId());
        item6.setCode("T006");
        item6.setName("猫三联疫苗");
        item6.setDepartmentId(3L);
        item6.setDepartmentName("疫苗科");
        item6.setPrice(new BigDecimal("120.00"));
        item6.setCost(new BigDecimal("50.00"));
        item6.setRequiredConsumableIds(Arrays.asList(1L));
        item6.setIsVaccine(true);
        item6.setNextVaccineDays(365);
        item6.setNeedFollowUp(false);
        item6.setDescription("猫三联疫苗接种");
        treatmentItems.put(item6.getId(), item6);

        TreatmentItem item7 = new TreatmentItem();
        item7.setId(generateId());
        item7.setCode("T007");
        item7.setName("犬六联疫苗");
        item7.setDepartmentId(3L);
        item7.setDepartmentName("疫苗科");
        item7.setPrice(new BigDecimal("100.00"));
        item7.setCost(new BigDecimal("40.00"));
        item7.setRequiredConsumableIds(Arrays.asList(1L));
        item7.setIsVaccine(true);
        item7.setNextVaccineDays(365);
        item7.setNeedFollowUp(false);
        item7.setDescription("犬六联疫苗接种");
        treatmentItems.put(item7.getId(), item7);
    }

    private void initCages() {
        Cage cage1 = new Cage();
        cage1.setId(generateId());
        cage1.setCode("CAGE001");
        cage1.setName("小型犬笼位1");
        cage1.setType("小型犬");
        cage1.setMaxWeight(10);
        cage1.setDailyRate(new BigDecimal("30.00"));
        cages.put(cage1.getId(), cage1);

        Cage cage2 = new Cage();
        cage2.setId(generateId());
        cage2.setCode("CAGE002");
        cage2.setName("小型犬笼位2");
        cage2.setType("小型犬");
        cage2.setMaxWeight(10);
        cage2.setDailyRate(new BigDecimal("30.00"));
        cages.put(cage2.getId(), cage2);

        Cage cage3 = new Cage();
        cage3.setId(generateId());
        cage3.setCode("CAGE003");
        cage3.setName("中型犬笼位1");
        cage3.setType("中型犬");
        cage3.setMaxWeight(25);
        cage3.setDailyRate(new BigDecimal("50.00"));
        cages.put(cage3.getId(), cage3);

        Cage cage4 = new Cage();
        cage4.setId(generateId());
        cage4.setCode("CAGE004");
        cage4.setName("猫笼位1");
        cage4.setType("猫");
        cage4.setMaxWeight(8);
        cage4.setDailyRate(new BigDecimal("25.00"));
        cages.put(cage4.getId(), cage4);

        Cage cage5 = new Cage();
        cage5.setId(generateId());
        cage5.setCode("CAGE005");
        cage5.setName("猫笼位2");
        cage5.setType("猫");
        cage5.setMaxWeight(8);
        cage5.setDailyRate(new BigDecimal("25.00"));
        cages.put(cage5.getId(), cage5);
    }

    private void initOwnersAndPets() {
        Owner owner1 = new Owner();
        owner1.setId(generateId());
        owner1.setName("张三");
        owner1.setPhone("13900139001");
        owner1.setIdCard("110101199001011234");
        owner1.setAddress("北京市朝阳区xxx小区1号楼");
        owner1.setMemberLevel(MemberLevel.SILVER);
        owner1.setPoints(500);
        owners.put(owner1.getId(), owner1);

        Owner owner2 = new Owner();
        owner2.setId(generateId());
        owner2.setName("李四");
        owner2.setPhone("13900139002");
        owner2.setIdCard("110101198505055678");
        owner2.setAddress("北京市海淀区xxx小区2号楼");
        owner2.setMemberLevel(MemberLevel.GOLD);
        owner2.setPoints(1500);
        owners.put(owner2.getId(), owner2);

        Owner owner3 = new Owner();
        owner3.setId(generateId());
        owner3.setName("王五");
        owner3.setPhone("13900139003");
        owner3.setIdCard("110101199212129012");
        owner3.setAddress("北京市西城区xxx小区3号楼");
        owner3.setMemberLevel(MemberLevel.NORMAL);
        owner3.setPoints(0);
        owners.put(owner3.getId(), owner3);

        Pet pet1 = new Pet();
        pet1.setId(generateId());
        pet1.setName("旺财");
        pet1.setBreed("金毛");
        pet1.setAge(3);
        pet1.setWeight(new BigDecimal("25.5"));
        pet1.setGender("公");
        pet1.setAllergicMedicines(Arrays.asList("头孢拉定"));
        pet1.setOwnerId(1L);
        pet1.setNotes("性格温顺");
        pets.put(pet1.getId(), pet1);

        Pet pet2 = new Pet();
        pet2.setId(generateId());
        pet2.setName("咪咪");
        pet2.setBreed("英短");
        pet2.setAge(2);
        pet2.setWeight(new BigDecimal("4.5"));
        pet2.setGender("母");
        pet2.setAllergicMedicines(new ArrayList<>());
        pet2.setOwnerId(2L);
        pet2.setNotes("有点胆小");
        pets.put(pet2.getId(), pet2);

        Pet pet3 = new Pet();
        pet3.setId(generateId());
        pet3.setName("小黑");
        pet3.setBreed("拉布拉多");
        pet3.setAge(1);
        pet3.setWeight(new BigDecimal("20.0"));
        pet3.setGender("公");
        pet3.setAllergicMedicines(Arrays.asList("布洛芬"));
        pet3.setOwnerId(3L);
        pet3.setNotes("活泼好动");
        pets.put(pet3.getId(), pet3);
    }

    private void initSchedules() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfterTomorrow = today.plusDays(2);

        Schedule schedule1 = new Schedule();
        schedule1.setId(generateId());
        schedule1.setDoctorId(1L);
        schedule1.setDoctorName("张医生");
        schedule1.setDepartmentId(1L);
        schedule1.setDepartmentName("内科");
        schedule1.setDate(today);
        schedule1.setStartTime(LocalTime.of(9, 0));
        schedule1.setEndTime(LocalTime.of(12, 0));
        schedule1.setMaxPatients(10);
        schedule1.setBookedPatients(3);
        schedules.put(schedule1.getId(), schedule1);

        Schedule schedule2 = new Schedule();
        schedule2.setId(generateId());
        schedule2.setDoctorId(1L);
        schedule2.setDoctorName("张医生");
        schedule2.setDepartmentId(1L);
        schedule2.setDepartmentName("内科");
        schedule2.setDate(today);
        schedule2.setStartTime(LocalTime.of(14, 0));
        schedule2.setEndTime(LocalTime.of(18, 0));
        schedule2.setMaxPatients(10);
        schedule2.setBookedPatients(5);
        schedules.put(schedule2.getId(), schedule2);

        Schedule schedule3 = new Schedule();
        schedule3.setId(generateId());
        schedule3.setDoctorId(2L);
        schedule3.setDoctorName("李医生");
        schedule3.setDepartmentId(2L);
        schedule3.setDepartmentName("外科");
        schedule3.setDate(tomorrow);
        schedule3.setStartTime(LocalTime.of(9, 0));
        schedule3.setEndTime(LocalTime.of(12, 0));
        schedule3.setMaxPatients(5);
        schedule3.setBookedPatients(2);
        schedules.put(schedule3.getId(), schedule3);

        Schedule schedule4 = new Schedule();
        schedule4.setId(generateId());
        schedule4.setDoctorId(3L);
        schedule4.setDoctorName("王医生");
        schedule4.setDepartmentId(3L);
        schedule4.setDepartmentName("疫苗科");
        schedule4.setDate(dayAfterTomorrow);
        schedule4.setStartTime(LocalTime.of(9, 0));
        schedule4.setEndTime(LocalTime.of(17, 0));
        schedule4.setMaxPatients(20);
        schedule4.setBookedPatients(8);
        schedules.put(schedule4.getId(), schedule4);
    }

    // Getters for all maps
    public Map<Long, Department> getDepartments() { return departments; }
    public Map<Long, Doctor> getDoctors() { return doctors; }
    public Map<Long, Owner> getOwners() { return owners; }
    public Map<Long, Pet> getPets() { return pets; }
    public Map<Long, Medicine> getMedicines() { return medicines; }
    public Map<Long, MedicineBatch> getMedicineBatches() { return medicineBatches; }
    public Map<Long, Consumable> getConsumables() { return consumables; }
    public Map<Long, TreatmentItem> getTreatmentItems() { return treatmentItems; }
    public Map<Long, Schedule> getSchedules() { return schedules; }
    public Map<Long, Cage> getCages() { return cages; }
    public Map<Long, Consultation> getConsultations() { return consultations; }
    public Map<Long, Prescription> getPrescriptions() { return prescriptions; }
    public Map<Long, Boarding> getBoardings() { return boardings; }
    public Map<Long, Bill> getBills() { return bills; }
    public Map<Long, Reminder> getReminders() { return reminders; }
}
