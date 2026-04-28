import React, { useState, useEffect } from 'react';
import { Table, Button, Modal, Form, Input, Select, InputNumber, message, Popconfirm, Tag } from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, SearchOutlined } from '@ant-design/icons';
import axios from 'axios';

const { Option } = Select;
const { Search } = Input;

const OwnersPage = () => {
  const [owners, setOwners] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingOwner, setEditingOwner] = useState(null);
  const [searchText, setSearchText] = useState('');
  const [form] = Form.useForm();

  useEffect(() => {
    loadOwners();
  }, []);

  const loadOwners = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/owners');
      setOwners(res.data.data || []);
    } catch (error) {
      message.error('加载主人列表失败');
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = () => {
    setEditingOwner(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleEdit = (record) => {
    setEditingOwner(record);
    form.setFieldsValue(record);
    setModalVisible(true);
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`/api/owners/${id}`);
      message.success('删除成功');
      loadOwners();
    } catch (error) {
      message.error('删除失败');
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      if (editingOwner) {
        await axios.put(`/api/owners/${editingOwner.id}`, values);
        message.success('更新成功');
      } else {
        await axios.post('/api/owners', values);
        message.success('创建成功');
      }
      setModalVisible(false);
      loadOwners();
    } catch (error) {
      message.error('操作失败');
    }
  };

  const filteredOwners = owners.filter(owner => {
    if (!searchText) return true;
    return owner.name?.toLowerCase().includes(searchText.toLowerCase()) ||
           owner.phone?.includes(searchText);
  });

  const getMemberLevelColor = (level) => {
    const colors = {
      'NORMAL': 'default',
      'SILVER': 'blue',
      'GOLD': 'gold',
      'PLATINUM': 'purple'
    };
    return colors[level] || 'default';
  };

  const getMemberLevelDesc = (level) => {
    const descs = {
      'NORMAL': '普通会员',
      'SILVER': '银卡会员',
      'GOLD': '金卡会员',
      'PLATINUM': '铂金会员'
    };
    return descs[level] || level;
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '姓名', dataIndex: 'name', key: 'name', width: 100 },
    { title: '电话', dataIndex: 'phone', key: 'phone', width: 130 },
    { title: '身份证', dataIndex: 'idCard', key: 'idCard', width: 180 },
    { title: '地址', dataIndex: 'address', key: 'address', width: 200 },
    { 
      title: '会员等级', 
      dataIndex: 'memberLevel', 
      key: 'memberLevel',
      width: 100,
      render: (level) => <Tag color={getMemberLevelColor(level)}>{getMemberLevelDesc(level)}</Tag>
    },
    { title: '积分', dataIndex: 'points', key: 'points', width: 80 },
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
        <Button type="primary" icon={<PlusOutlined />} onClick={handleCreate}>添加主人</Button>
        <Search
          placeholder="搜索姓名或电话"
          allowClear
          enterButton={<SearchOutlined />}
          style={{ width: 300 }}
          onSearch={setSearchText}
          onChange={(e) => setSearchText(e.target.value)}
        />
      </div>

      <Table
        columns={columns}
        dataSource={filteredOwners}
        rowKey="id"
        loading={loading}
        scroll={{ x: 1000 }}
      />

      <Modal
        title={editingOwner ? '编辑主人' : '添加主人'}
        open={modalVisible}
        onOk={handleSubmit}
        onCancel={() => setModalVisible(false)}
        width={600}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="姓名" rules={[{ required: true, message: '请输入姓名' }]}>
            <Input placeholder="请输入姓名" />
          </Form.Item>
          <Form.Item name="phone" label="电话" rules={[{ required: true, message: '请输入电话' }]}>
            <Input placeholder="请输入电话" />
          </Form.Item>
          <Form.Item name="idCard" label="身份证号">
            <Input placeholder="请输入身份证号" />
          </Form.Item>
          <Form.Item name="address" label="地址">
            <Input.TextArea rows={2} placeholder="请输入地址" />
          </Form.Item>
          <Form.Item name="notes" label="备注">
            <Input.TextArea rows={2} placeholder="备注信息" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default OwnersPage;
