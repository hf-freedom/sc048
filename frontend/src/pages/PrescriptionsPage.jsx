import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, message } from 'antd';
import axios from 'axios';

const PrescriptionsPage = () => {
  const [prescriptions, setPrescriptions] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadPrescriptions();
  }, []);

  const loadPrescriptions = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/prescriptions');
      setPrescriptions(res.data.data || []);
    } catch (error) {
      message.error('加载处方列表失败');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      'PENDING': 'gold',
      'DISPENSED': 'blue',
      'CANCELLED': 'red',
      'REFUNDED': 'orange'
    };
    return colors[status] || 'default';
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '编号', dataIndex: 'code', key: 'code', width: 100 },
    { title: '宠物', dataIndex: 'petName', key: 'petName', width: 100 },
    { title: '主人', dataIndex: 'ownerName', key: 'ownerName', width: 100 },
    { title: '医生', dataIndex: 'doctorName', key: 'doctorName', width: 100 },
    { 
      title: '状态', 
      dataIndex: 'status', 
      key: 'status',
      width: 100,
      render: (status) => <Tag color={getStatusColor(status)}>{status}</Tag>
    },
    { title: '总金额', dataIndex: 'totalAmount', key: 'totalAmount', width: 100 },
    { title: '折扣金额', dataIndex: 'discountAmount', key: 'discountAmount', width: 100 },
    { title: '应付金额', dataIndex: 'payableAmount', key: 'payableAmount', width: 100 }
  ];

  return (
    <Card title="处方列表">
      <Table
        columns={columns}
        dataSource={prescriptions}
        rowKey="id"
        loading={loading}
        scroll={{ x: 1000 }}
      />
    </Card>
  );
};

export default PrescriptionsPage;
