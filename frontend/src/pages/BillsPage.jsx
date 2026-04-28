import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, message } from 'antd';
import axios from 'axios';

const BillsPage = () => {
  const [bills, setBills] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadBills();
  }, []);

  const loadBills = async () => {
    setLoading(true);
    try {
      const res = await axios.get('/api/bills');
      setBills(res.data.data || []);
    } catch (error) {
      message.error('加载账单列表失败');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      'PENDING': 'gold',
      'PAID': 'green',
      'REFUNDED': 'orange'
    };
    return colors[status] || 'default';
  };

  const getTypeDesc = (type) => {
    const descs = {
      'CONSULTATION': '诊疗费',
      'BOARDING': '寄养费'
    };
    return descs[type] || type;
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '编号', dataIndex: 'code', key: 'code', width: 100 },
    { title: '类型', dataIndex: 'type', key: 'type', width: 100,
      render: (type) => getTypeDesc(type)
    },
    { title: '主人', dataIndex: 'ownerName', key: 'ownerName', width: 100 },
    { title: '关联编号', dataIndex: 'relatedCode', key: 'relatedCode', width: 100 },
    { 
      title: '状态', 
      dataIndex: 'status', 
      key: 'status',
      width: 100,
      render: (status) => <Tag color={getStatusColor(status)}>{status}</Tag>
    },
    { title: '总金额', dataIndex: 'totalAmount', key: 'totalAmount', width: 100 },
    { title: '折扣金额', dataIndex: 'discountAmount', key: 'discountAmount', width: 100 },
    { title: '应付金额', dataIndex: 'payableAmount', key: 'payableAmount', width: 100 },
    { title: '已付金额', dataIndex: 'paidAmount', key: 'paidAmount', width: 100 },
    { title: '支付方式', dataIndex: 'paymentMethod', key: 'paymentMethod', width: 100 }
  ];

  return (
    <Card title="账单列表">
      <Table
        columns={columns}
        dataSource={bills}
        rowKey="id"
        loading={loading}
        scroll={{ x: 1200 }}
      />
    </Card>
  );
};

export default BillsPage;
