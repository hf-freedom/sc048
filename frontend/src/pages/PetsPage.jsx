import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, Select, InputNumber, message, Popconfirm } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Option } = Select;
const { Search } = Input;

const PetsPage = () => {
  const [pets, setPets] = useState([]);
  const [owners, setOwners] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingPet, setEditingPet] = useState(null);
  const [searchText, setSearchText] = useState('');
  const [form] = Form.useForm();

  useEffect(() => {
    loadPets();
    loadOwners();
  }, []);

  const loadPets = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/pets');
      setPets(res.data.data || []);
    } catch (error) {
      message.error('加载宠物列表失败');
    } finally {
      setLoading(false);
    }
  };

  const loadOwners = async () => {
    try {
      const res = await axios.get('/api/owners');
      setOwners(res.data.data || []);
    } catch (error) {
      console.error('加载主人列表失败');
    }
  };

  const handleCreate = () => {
    setEditingPet(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleEdit = (record) => {
    setEditingPet(record);
    form.setFieldsValue({
      ...record,
      allergicMedicines: record.allergicMedicines?.join(', ') || ''
    });
    setModalVisible(true);
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/pets/${id}`);
      message.success('删除成功');
      loadPets();
    } catch (error) {
      message.error('删除失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();
      
      if (values.allergicMedicines) {
        values.allergicMedicines = values.allergicMedicines.split(',').map(s => s.trim()).filter(Boolean);
      } else {
        values.allergicMedicines = [];
      }

      if (editingPet) {
        await axios.put(`/api/pets/${editingPet.id}`, values);
        message.success('更新成功');
      } else {
        await axios.post('/api/pets', values);
        message.success('创建成功');
      }
      setModalVisible(false);
      loadPets();
    } catch (error) {
      message.error('操作失败');
    }
  };

  const filteredPets = pets.filter(pet => {
    if (!searchText) return true;
    return pet.name?.toLowerCase().includes(searchText.toLowerCase()) ||
           pet.breed?.toLowerCase().includes(searchText.toLowerCase());
  });

  const getOwnerName = (ownerId) => {
    const owner = owners.find(o => o.id === ownerId);
    return owner?.name || '-';
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '名字', dataIndex: 'name', key: 'name', width: 100 },
    { title: '品种', dataIndex: 'breed', key: 'breed', width: 120 },
    { title: '年龄(岁)', dataIndex: 'age', key: 'age', width: 80 },
    { title: '体重(kg)', dataIndex: 'weight', key: 'weight', width: 100 },
    { title: '性别', dataIndex: 'gender', key: 'gender', width: 80 },
    { 
      title: '过敏药品', 
      dataIndex: 'allergicMedicines', 
      key: 'allergicMedicines',
      width: 150,
      render: (arr) => arr?.join(', ') || '-'
    },
    { 
      title: '主人', 
      dataIndex: 'ownerId', 
      key: 'ownerId',
      width: 100,
      render: (id) => getOwnerName(id)
    },
    {
      title: '操作',
      key: 'action',
      width: 150,
      render: (_, record) => (
        <span>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>编辑</Button>
          <Popconfirm title="确定删除吗？" onConfirm={() => handleDelete(record.id)}>
            <Button type="link" danger icon={<DeleteOutlined />}>删除</Button>
          </Popconfirm>
        </span>
      )
    }
  ];

  return (
    <div>
      <div className="action-bar">
        <Button type="primary" icon={<PlusOutlined />} onClick={handleCreate}>添加宠物</Button>
        <Search
          placeholder="搜索宠物名称或品种"
          allowClear
          enterButton={<SearchOutlined />}
          style={{ width: 300 }}
          onSearch={setSearchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
      </div>

      <Table
        columns={columns}
        dataSource={filteredPets}
        rowKey="id"
        loading={loading}
        scroll={{ x: 1200 }}
      />

      <Modal
        title={editingPet ? '编辑宠物' : '添加宠物'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="名字" rules={[{ required: true, message: '请输入名字' }]}>
            <Input placeholder="请输入宠物名字" />
          </Form.Item>
          <Form.Item name="breed" label="品种" rules={[{ required: true, message: '请输入品种' }]}>
            <Input placeholder="请输入品种" />
          </Form.Item>
          <Form.Item name="age" label="年龄(岁)" rules={[{ required: true, message: '请输入年龄' }]}>
            <InputNumber min={0} max={50} style={{ width: '100%' }} placeholder="请输入年龄" />
          </Form.Item>
          <Form.Item name="weight" label="体重(kg)" rules={[{ required: true, message: '请输入体重' }]}>
            <InputNumber min={0} max={200} precision={2} style={{ width: '100%' }} placeholder="请输入体重" />
          </Form.Item>
          <Form.Item name="gender" label="性别" rules={[{ required: true, message: '请选择性别' }]}>
            <Select placeholder="请选择性别">
              <Option value="公">公</Option>
              <Option value="母">母</Option>
            </Select>
          </Form.Item>
          <Form.Item name="ownerId" label="主人" rules={[{ required: true, message: '请选择主人' }]}>
            <Select placeholder="请选择主人" showSearch optionFilterProp="children">
              {owners.map(owner => (
                <Option key={owner.id} value={owner.id}>{owner.name} - {owner.phone}</Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="allergicMedicines" label="过敏药品(多个用逗号分隔)">
            <Input placeholder="例如: 青霉素, 头孢" />
          </Form.Item>
          <Form.Item name="notes" label="备注">
            <Input.TextArea rows={3} placeholder="备注信息" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default PetsPage;
