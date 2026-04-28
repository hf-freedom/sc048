import React, { useState, useEffect } from 'react';
import {
  Table, Card, Tag, message, Button, Modal, Form, Select, Input,
  InputNumber, DatePicker, TimePicker, Space, Popconfirm, Row, Col,
  Divider, Statistic, Descriptions, List, Typography
} from 'antd';
import {
  PlusOutlined, EditOutlined, DeleteOutlined, MedicineBoxOutlined,
  CheckCircleOutlined, FileTextOutlined, WalletOutlined
} from '@ant-design/icons';
import axios from 'axios';
import dayjs from 'dayjs';

const { TextArea } = Input;
const { Option } = Select;
const { Text } = Typography;

const ConsultationsPage = () => {
  const [consultations, setConsultations] = useState([]);
  const [loading, setLoading] = useState(false);
  const [createModalVisible, setCreateModalVisible] = useState(false);
  const [prescriptionModalVisible, setPrescriptionModalVisible] = useState(false);
  const [detailModalVisible, setDetailModalVisible] = useState(false);
  const [selectedConsultation, setSelectedConsultation] = useState(null);
  const [form] = Form.useForm();
  const [prescriptionForm] = Form.useForm();

  const [pets, setPets] = useState([]);
  const [owners, setOwners] = useState([]);
  const [doctors, setDoctors] = useState([]);
  const [schedules, setSchedules] = useState([]);
  const [treatments, setTreatments] = useState([]);
  const [medicines, setMedicines] = useState([]);

  const [selectedOwner, setSelectedOwner] = useState(null);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [selectedSchedule, setSelectedSchedule] = useState(null);
  const [prescriptionItems, setPrescriptionItems] = useState([]);

  useEffect(() => {
    loadAllData();
  }, []);

  const loadAllData = async () => {
    setLoading(true);
    try {
      const [consultationsRes, petsRes, ownersRes, doctorsRes, treatmentsRes, medicinesRes] = await Promise.all([
        axios.get('/api/consultations'),
        axios.get('/api/pets'),
        axios.get('/api/owners'),
        axios.get('/api/doctors'),
        axios.get('/api/treatments'),
        axios.get('/api/medicines')
      ]);
      setConsultations(consultationsRes.data.data || []);
      setPets(petsRes.data.data || []);
      setOwners(ownersRes.data.data || []);
      setDoctors(doctorsRes.data.data || []);
      setTreatments(treatmentsRes.data.data || []);
      setMedicines(medicinesRes.data.data || []);
    } catch (error) {
      console.error('加载数据失败:', error);
      message.error('加载数据失败');
    } finally {
      setLoading(false);
    }
  };

  const loadSchedules = async (doctorId, startDate, endDate) => {
    try {
      const params = {
        startDate: startDate || dayjs().format('YYYY-MM-DD'),
        endDate: endDate || dayjs().add(7, 'day').format('YYYY-MM-DD')
      };
      if (doctorId) {
        const res = await axios.get(`/api/doctors/${doctorId}/schedules`, { params });
        setSchedules(res.data.data || []);
      }
    } catch (error) {
      console.error('加载排班失败:', error);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      'PENDING': 'gold',
      'IN_PROGRESS': 'blue',
      'COMPLETED': 'green',
      'CANCELLED': 'red'
    };
    return colors[status] || 'default';
  };

  const getStatusText = (status) => {
    const texts = {
      'PENDING': '待接诊',
      'IN_PROGRESS': '诊疗中',
      'COMPLETED': '已完成',
      'CANCELLED': '已取消'
    };
    return texts[status] || status;
  };

  const handleOwnerChange = (ownerId) => {
    setSelectedOwner(owners.find(o => o.id === ownerId));
  };

  const handleDoctorChange = (doctorId) => {
    const doctor = doctors.find(d => d.id === doctorId);
    setSelectedDoctor(doctor);
    if (doctor) {
      loadSchedules(doctorId);
    } else {
      setSchedules([]);
    }
  };

  const handleCreateConsultation = async (values) => {
    try {
      const selectedPet = pets.find(p => p.id === values.petId);
      const selectedOwner = owners.find(o => o.id === selectedPet?.ownerId);
      const selectedDoctor = doctors.find(d => d.id === values.doctorId);

      const consultationData = {
        petId: values.petId,
        petName: selectedPet?.name,
        ownerId: selectedOwner?.id,
        ownerName: selectedOwner?.name,
        doctorId: values.doctorId,
        doctorName: selectedDoctor?.name,
        departmentId: selectedDoctor?.departmentId,
        departmentName: selectedDoctor?.departmentName,
        scheduleId: values.scheduleId,
        consultationTime: dayjs(values.consultationTime).format('YYYY-MM-DD HH:mm:ss'),
        treatmentItemIds: values.treatmentItemIds,
        chiefComplaint: values.chiefComplaint
      };

      const res = await axios.post('/api/consultations', consultationData);
      message.success('创建就诊成功！');
      setCreateModalVisible(false);
      form.resetFields();
      loadAllData();
    } catch (error) {
      console.error('创建就诊失败:', error);
      message.error(error.response?.data?.message || '创建就诊失败');
    }
  };

  const handleCompleteConsultation = async (id) => {
    try {
      await axios.post(`/api/consultations/${id}/complete`);
      message.success('完成就诊成功！');
      loadAllData();
    } catch (error) {
      message.error(error.response?.data?.message || '完成就诊失败');
    }
  };

  const handleAddPrescriptionItem = () => {
    const newItem = {
      id: Date.now(),
      medicineId: null,
      quantity: 1,
      dosage: '',
      frequency: '',
      notes: ''
    };
    setPrescriptionItems([...prescriptionItems, newItem]);
  };

  const handleRemovePrescriptionItem = (id) => {
    setPrescriptionItems(prescriptionItems.filter(item => item.id !== id));
  };

  const handleUpdatePrescriptionItem = (id, field, value) => {
    setPrescriptionItems(prescriptionItems.map(item =>
      item.id === id ? { ...item, [field]: value } : item
    ));
  };

  const handleCreatePrescription = async () => {
    if (!selectedConsultation) {
      message.error('请先选择就诊记录');
      return;
    }

    if (prescriptionItems.length === 0) {
      message.error('请至少添加一种药品');
      return;
    }

    const validItems = prescriptionItems.filter(item => item.medicineId);
    if (validItems.length === 0) {
      message.error('请选择药品');
      return;
    }

    const pet = pets.find(p => p.id === selectedConsultation.petId);

    const prescriptionData = {
      consultationId: selectedConsultation.id,
      petId: selectedConsultation.petId,
      petName: selectedConsultation.petName,
      ownerId: selectedConsultation.ownerId,
      ownerName: selectedConsultation.ownerName,
      doctorId: selectedConsultation.doctorId,
      doctorName: selectedConsultation.doctorName,
      items: validItems.map(item => {
        const medicine = medicines.find(m => m.id === item.medicineId);
        return {
          medicineId: item.medicineId,
          medicineName: medicine?.name,
          specification: medicine?.specification,
          unit: medicine?.unit,
          quantity: item.quantity,
          unitPrice: medicine?.salePrice,
          dosage: item.dosage,
          frequency: item.frequency,
          notes: item.notes
        };
      })
    };

    try {
      await axios.post(`/api/consultations/${selectedConsultation.id}/prescription`, prescriptionData);
      message.success('创建处方成功！');
      setPrescriptionModalVisible(false);
      setPrescriptionItems([]);
      loadAllData();
    } catch (error) {
      console.error('创建处方失败:', error);
      message.error(error.response?.data?.message || '创建处方失败');
    }
  };

  const handleCreateBill = async (id) => {
    try {
      const res = await axios.post(`/api/consultations/${id}/bill`);
      message.success('生成账单成功！');
      loadAllData();
    } catch (error) {
      message.error(error.response?.data?.message || '生成账单失败');
    }
  };

  const handlePayBill = async (billId, consultationId) => {
    try {
      await axios.post(`/api/bills/${billId}/pay?paymentMethod=CASH`, {});
      message.success('支付成功！');
      loadAllData();
    } catch (error) {
      message.error(error.response?.data?.message || '支付失败');
    }
  };

  const getOwnerPets = (ownerId) => {
    return pets.filter(p => p.ownerId === ownerId);
  };

  const getDoctorTreatments = (doctor) => {
    if (!doctor || !doctor.authorizedTreatmentIds) return [];
    return treatments.filter(t => doctor.authorizedTreatmentIds.includes(t.id));
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '编号', dataIndex: 'code', key: 'code', width: 120 },
    { title: '宠物', dataIndex: 'petName', key: 'petName', width: 100 },
    { title: '主人', dataIndex: 'ownerName', key: 'ownerName', width: 100 },
    { title: '医生', dataIndex: 'doctorName', key: 'doctorName', width: 100 },
    { title: '科室', dataIndex: 'departmentName', key: 'departmentName', width: 100 },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      width: 100,
      render: (status) => <Tag color={getStatusColor(status)}>{getStatusText(status)}</Tag>
    },
    {
      title: '需要复诊',
      dataIndex: 'needFollowUp',
      key: 'needFollowUp',
      width: 100,
      render: (need) => need ? <Tag color="red">是</Tag> : <Tag color="green">否</Tag>
    },
    {
      title: '操作',
      key: 'action',
      width: 300,
      render: (_, record) => (
        <Space size="small">
          <Button
            type="link"
            icon={<FileTextOutlined />}
            onClick={() => {
              setSelectedConsultation(record);
              setDetailModalVisible(true);
            }}
          >
            详情
          </Button>
          {record.status === 'IN_PROGRESS' && (
            <>
              <Button
                type="link"
                icon={<MedicineBoxOutlined />}
                onClick={() => {
                  setSelectedConsultation(record);
                  setPrescriptionItems([]);
                  setPrescriptionModalVisible(true);
                }}
              >
                开处方
              </Button>
              <Popconfirm title="确定完成就诊吗？" onConfirm={() => handleCompleteConsultation(record.id)}>
                <Button type="link" icon={<CheckCircleOutlined />} style={{ color: '#52c41a' }}>
                  完成就诊
                </Button>
              </Popconfirm>
            </>
          )}
          {record.status === 'COMPLETED' && !record.billId && (
            <Button
              type="link"
              icon={<WalletOutlined />}
              onClick={() => handleCreateBill(record.id)}
            >
              生成账单
            </Button>
          )}
        </Space>
      )
    }
  ];

  return (
    <div>
      <Card>
        <Row justify="space-between" align="middle" style={{ marginBottom: 16 }}>
          <Col>
            <Space>
              <Statistic title="总就诊数" value={consultations.length} />
              <Statistic
                title="诊疗中"
                value={consultations.filter(c => c.status === 'IN_PROGRESS').length}
                valueStyle={{ color: '#1890ff' }}
              />
              <Statistic
                title="已完成"
                value={consultations.filter(c => c.status === 'COMPLETED').length}
                valueStyle={{ color: '#52c41a' }}
              />
            </Space>
          </Col>
          <Col>
            <Button type="primary" icon={<PlusOutlined />} onClick={() => setCreateModalVisible(true)}>
              新建就诊
            </Button>
          </Col>
        </Row>

        <Table
          columns={columns}
          dataSource={consultations}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1200 }}
          pagination={{ pageSize: 10 }}
        />
      </Card>

      <Modal
        title="新建就诊"
        open={createModalVisible}
        onOk={() => form.submit()}
        onCancel={() => setCreateModalVisible(false)}
        width={800}
        okText="创建"
        cancelText="取消"
      >
        <Form
          form={form}
          layout="vertical"
          onFinish={handleCreateConsultation}
        >
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="doctorId" label="选择医生" rules={[{ required: true, message: '请选择医生' }]}>
                <Select
                  placeholder="请选择医生"
                  showSearch
                  optionFilterProp="children"
                  onChange={handleDoctorChange}
                >
                  {doctors.map(doctor => (
                    <Option key={doctor.id} value={doctor.id}>
                      {doctor.name} - {doctor.title} ({doctor.departmentName})
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="scheduleId" label="选择排班">
                <Select
                  placeholder="请先选择医生"
                  disabled={!selectedDoctor}
                  showSearch
                  optionFilterProp="children"
                  onChange={(value) => setSelectedSchedule(schedules.find(s => s.id === value))}
                >
                  {schedules.map(schedule => (
                    <Option key={schedule.id} value={schedule.id}>
                      {schedule.date} {schedule.startTime}-{schedule.endTime} (已预约{schedule.bookedPatients}/{schedule.maxPatients})
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            </Col>
          </Row>

          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="petId" label="选择宠物" rules={[{ required: true, message: '请选择宠物' }]}>
                <Select
                  placeholder="请选择宠物"
                  showSearch
                  optionFilterProp="children"
                >
                  {pets.map(pet => {
                    const owner = owners.find(o => o.id === pet.ownerId);
                    return (
                      <Option key={pet.id} value={pet.id}>
                        {pet.name} ({pet.breed}, {pet.age}岁, {pet.weight}kg) - 主人: {owner?.name}
                      </Option>
                    );
                  })}
                </Select>
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item
                name="consultationTime"
                label="就诊时间"
                rules={[{ required: true, message: '请选择就诊时间' }]}
                initialValue={dayjs()}
              >
                <DatePicker
                  showTime
                  format="YYYY-MM-DD HH:mm:ss"
                  style={{ width: '100%' }}
                />
              </Form.Item>
            </Col>
          </Row>

          <Form.Item
            name="treatmentItemIds"
            label="选择诊疗项目"
            rules={[{ required: true, message: '请至少选择一个诊疗项目' }]}
          >
            <Select
              mode="multiple"
              placeholder={selectedDoctor ? '请选择诊疗项目' : '请先选择医生'}
              disabled={!selectedDoctor}
              optionFilterProp="children"
            >
              {getDoctorTreatments(selectedDoctor).map(treatment => (
                <Option key={treatment.id} value={treatment.id}>
                  {treatment.name} - ¥{treatment.price}
                  {treatment.needFollowUp && ' [需复诊]'}
                  {treatment.isVaccine && ' [疫苗]'}
                </Option>
              ))}
            </Select>
          </Form.Item>

          <Form.Item name="chiefComplaint" label="主诉">
            <TextArea rows={3} placeholder="请输入宠物症状描述" />
          </Form.Item>

          {selectedDoctor && (
            <Card size="small" title="医生信息">
              <Descriptions size="small" column={2}>
                <Descriptions.Item label="姓名">{selectedDoctor.name}</Descriptions.Item>
                <Descriptions.Item label="职称">{selectedDoctor.title}</Descriptions.Item>
                <Descriptions.Item label="科室">{selectedDoctor.departmentName}</Descriptions.Item>
                <Descriptions.Item label="电话">{selectedDoctor.phone}</Descriptions.Item>
              </Descriptions>
            </Card>
          )}
        </Form>
      </Modal>

      <Modal
        title="开处方"
        open={prescriptionModalVisible}
        onOk={handleCreatePrescription}
        onCancel={() => {
          setPrescriptionModalVisible(false);
          setPrescriptionItems([]);
        }}
        width={900}
        okText="提交处方"
        cancelText="取消"
      >
        {selectedConsultation && (
          <>
            <Card size="small" style={{ marginBottom: 16 }}>
              <Descriptions size="small" column={3}>
                <Descriptions.Item label="就诊编号">{selectedConsultation.code}</Descriptions.Item>
                <Descriptions.Item label="宠物">{selectedConsultation.petName}</Descriptions.Item>
                <Descriptions.Item label="医生">{selectedConsultation.doctorName}</Descriptions.Item>
              </Descriptions>
            </Card>

            {(() => {
              const pet = pets.find(p => p.id === selectedConsultation.petId);
              if (pet && pet.allergicMedicines && pet.allergicMedicines.length > 0) {
                return (
                  <div style={{ marginBottom: 16, padding: 8, background: '#fff2f0', border: '1px solid #ffccc7', borderRadius: 4 }}>
                    <Text type="danger">
                      <strong>⚠️ 警告：</strong>该宠物对以下药品过敏：{pet.allergicMedicines.join('、')}
                    </Text>
                  </div>
                );
              }
              return null;
            })()}

            <Button type="dashed" onClick={handleAddPrescriptionItem} block style={{ marginBottom: 16 }}>
              <PlusOutlined /> 添加药品
            </Button>

            {prescriptionItems.length > 0 && (
              <List
                bordered
                dataSource={prescriptionItems}
                renderItem={(item, index) => (
                  <List.Item>
                    <Row gutter={8} style={{ width: '100%' }} align="middle">
                      <Col flex="1">
                        <Select
                          placeholder="选择药品"
                          style={{ width: '100%' }}
                          value={item.medicineId}
                          onChange={(value) => handleUpdatePrescriptionItem(item.id, 'medicineId', value)}
                          showSearch
                          optionFilterProp="children"
                        >
                          {medicines.map(medicine => {
                            const pet = pets.find(p => p.id === selectedConsultation.petId);
                            const isAllergic = pet?.allergicMedicines?.includes(medicine.name);
                            return (
                              <Option key={medicine.id} value={medicine.id} disabled={isAllergic}>
                                {medicine.name} ({medicine.specification}) - ¥{medicine.salePrice}
                                {isAllergic && ' ⚠️过敏'}
                              </Option>
                            );
                          })}
                        </Select>
                      </Col>
                      <Col flex="100px">
                        <InputNumber
                          min={1}
                          placeholder="数量"
                          style={{ width: '100%' }}
                          value={item.quantity}
                          onChange={(value) => handleUpdatePrescriptionItem(item.id, 'quantity', value)}
                        />
                      </Col>
                      <Col flex="120px">
                        <Input
                          placeholder="用量"
                          value={item.dosage}
                          onChange={(e) => handleUpdatePrescriptionItem(item.id, 'dosage', e.target.value)}
                        />
                      </Col>
                      <Col flex="120px">
                        <Input
                          placeholder="频次"
                          value={item.frequency}
                          onChange={(e) => handleUpdatePrescriptionItem(item.id, 'frequency', e.target.value)}
                        />
                      </Col>
                      <Col flex="80px">
                        <Button
                          type="link"
                          danger
                          onClick={() => handleRemovePrescriptionItem(item.id)}
                        >
                          删除
                        </Button>
                      </Col>
                    </Row>
                  </List.Item>
                )}
              />
            )}
          </>
        )}
      </Modal>

      <Modal
        title="就诊详情"
        open={detailModalVisible}
        onCancel={() => setDetailModalVisible(false)}
        footer={[
          <Button key="close" onClick={() => setDetailModalVisible(false)}>
            关闭
          </Button>
        ]}
        width={800}
      >
        {selectedConsultation && (
          <>
            <Descriptions bordered column={2}>
              <Descriptions.Item label="就诊编号">{selectedConsultation.code}</Descriptions.Item>
              <Descriptions.Item label="状态">
                <Tag color={getStatusColor(selectedConsultation.status)}>
                  {getStatusText(selectedConsultation.status)}
                </Tag>
              </Descriptions.Item>
              <Descriptions.Item label="宠物">{selectedConsultation.petName}</Descriptions.Item>
              <Descriptions.Item label="主人">{selectedConsultation.ownerName}</Descriptions.Item>
              <Descriptions.Item label="医生">{selectedConsultation.doctorName}</Descriptions.Item>
              <Descriptions.Item label="科室">{selectedConsultation.departmentName}</Descriptions.Item>
              <Descriptions.Item label="需要复诊">
                {selectedConsultation.needFollowUp ? <Tag color="red">是</Tag> : <Tag color="green">否</Tag>}
              </Descriptions.Item>
              <Descriptions.Item label="账单ID">
                {selectedConsultation.billId || '未生成'}
              </Descriptions.Item>
              <Descriptions.Item label="处方ID" span={2}>
                {selectedConsultation.prescriptionId || '未开处方'}
              </Descriptions.Item>
              <Descriptions.Item label="主诉" span={2}>
                {selectedConsultation.chiefComplaint || '-'}
              </Descriptions.Item>
              <Descriptions.Item label="诊断" span={2}>
                {selectedConsultation.diagnosis || '-'}
              </Descriptions.Item>
              <Descriptions.Item label="治疗方案" span={2}>
                {selectedConsultation.treatmentPlan || '-'}
              </Descriptions.Item>
            </Descriptions>
          </>
        )}
      </Modal>
    </div>
  );
};

export default ConsultationsPage;
