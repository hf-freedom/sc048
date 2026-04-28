import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, message } from 'antd';
import axios from 'axios';

const BoardingsPage = () => {
  const [boardings, setBoardings] = useState([]);
  const [overdue, setOverdue] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [boardingsRes, overdueRes] = await Promise.all([
        axios.get('/api/boardings'),
        axios.get('/api/boardings/overdue')
      ]);
      setBoardings(boardingsRes.data.data || []);
      setOverdue(overdueRes.data.data || []);
    } catch (error) {
      message.error('加载寄养记录失败');
    } finally {
      setLoading(false);
    }
  };

  const getStatusColor = (status) => {
    const colors = {
      'PENDING': 'gold',
      'CHECKED_IN': 'blue',
      'COMPLETED': 'green',
      'CANCELLED': 'red'
    };
    return colors[status] || 'default';
  };

  const columns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '编号', dataIndex: 'code', key: 'code', width: 100 },
    { title: '宠物', dataIndex: 'petName', key: 'petName', width: 100 },
    { title: '主人', dataIndex: 'ownerName', key: 'ownerName', width: 100 },
    { title: '联系电话', dataIndex: 'ownerPhone', key: 'ownerPhone', width: 130 },
    { title: '笼位', dataIndex: 'cageName', key: 'cageName', width: 120 },
    { title: '入住日期', dataIndex: 'checkInDate', key: 'checkInDate', width: 120 },
    { title: '预计退房', dataIndex: 'expectedCheckOutDate', key: 'expectedCheckOutDate', width: 120 },
    { title: '实际退房', dataIndex: 'actualCheckOutDate', key: 'actualCheckOutDate', width: 120 },
    { 
      title: '状态', 
      dataIndex: 'status', 
      key: 'status',
      width: 100,
      render: (status) => <Tag color={getStatusColor(status)}>{status}</Tag>
    },
    { title: '总金额', dataIndex: 'totalPayable', key: 'totalPayable', width: 100 }
  ];

  return (
    <div>
      {overdue.length > 0 && (
        <Card title="超期寄养提醒" style={{ marginBottom: 24, borderColor: '#ff4d4f' }}>
          <Table
            columns={columns}
            dataSource={overdue}
            rowKey="id"
            size="small"
            pagination={false}
            scroll={{ x: 1200 }}
          />
        </Card>
      )}
      <Card title="寄养记录">
        <Table
          columns={columns}
          dataSource={boardings}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1200 }}
        />
      </Card>
    </div>
  );
};

export default BoardingsPage;
