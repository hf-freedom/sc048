import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, Button, message, Space } from 'antd';
import { CheckOutlined, StopOutlined } from '@ant-design/icons';
import axios from 'axios';

const RemindersPage = () => {
  const [reminders, setReminders] = useState([]);
  const [pending, setPending] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [allRes, pendingRes] = await Promise.all([
        axios.get('/api/reminders'),
        axios.get('/api/reminders/pending')
      ]);
      setReminders(allRes.data.data || []);
      setPending(pendingRes.data.data || []);
    } catch (error) {
      message.error('加载提醒列表失败');
    } finally {
      setLoading(false);
    }
  };

  const getTypeDesc = (type) => {
    const descs = {
      'FOLLOW_UP': '复诊提醒',
      'VACCINE': '疫苗提醒',
      'BOARDING_OVERDUE': '寄养超期提醒'
    };
    return descs[type] || type;
  };

  const getTypeColor = (type) => {
    const colors = {
      'FOLLOW_UP': 'blue',
      'VACCINE': 'purple',
      'BOARDING_OVERDUE': 'red'
    };
    return colors[type] || 'default';
  };

  const getStatusColor = (status) => {
    const colors = {
      'PENDING': 'gold',
      'SENT': 'green',
      'CANCELLED': 'red'
    };
    return colors[status] || 'default';
  };

  const handleMarkAsSent = async (id) => {
    try {
      await axios.post(`/api/reminders/${id}/send`);
      message.success('已标记为已发送');
      loadData();
    } catch (error) {
      message.error('操作失败');
    }
  };

  const handleCancel = async (id) => {
    try {
      await axios.post(`/api/reminders/${id}/cancel`);
      message.success('已取消提醒');
      loadData();
    } catch (error) {
      message.error('操作失败');
    }
  };

  const pendingColumns = [
    { title: '类型', dataIndex: 'type', key: 'type', width: 120,
      render: (type) => <Tag color={getTypeColor(type)}>{getTypeDesc(type)}</Tag>
    },
    { title: '宠物', dataIndex: 'petName', key: 'petName', width: 100 },
    { title: '主人', dataIndex: 'ownerName', key: 'ownerName', width: 100 },
    { title: '联系电话', dataIndex: 'ownerPhone', key: 'ownerPhone', width: 130 },
    { title: '提醒日期', dataIndex: 'remindDate', key: 'remindDate', width: 120 },
    { title: '内容', dataIndex: 'content', key: 'content', width: 300, ellipsis: true },
    {
      title: '操作',
      key: 'action',
      width: 150,
      render: (_, record) => (
        <Space>
          <Button 
            type="link" 
            icon={<CheckOutlined />} 
            onClick={() => handleMarkAsSent(record.id)}
            style={{ color: '#52c41a' }}
          >
            标记已发送
          </Button>
          <Button 
            type="link" 
            danger 
            icon={<StopOutlined />} 
            onClick={() => handleCancel(record.id)}
          >
            取消
          </Button>
        </Space>
      )
    }
  ];

  const allColumns = [
    { title: '类型', dataIndex: 'type', key: 'type', width: 120,
      render: (type) => <Tag color={getTypeColor(type)}>{getTypeDesc(type)}</Tag>
    },
    { title: '宠物', dataIndex: 'petName', key: 'petName', width: 100 },
    { title: '主人', dataIndex: 'ownerName', key: 'ownerName', width: 100 },
    { title: '提醒日期', dataIndex: 'remindDate', key: 'remindDate', width: 120 },
    { 
      title: '状态', 
      dataIndex: 'status', 
      key: 'status',
      width: 100,
      render: (status) => <Tag color={getStatusColor(status)}>{status}</Tag>
    },
    { title: '内容', dataIndex: 'content', key: 'content', width: 300, ellipsis: true }
  ];

  return (
    <div>
      {pending.length > 0 && (
        <Card title="待处理提醒" style={{ marginBottom: 24, borderColor: '#faad14' }}>
          <Table
            columns={pendingColumns}
            dataSource={pending}
            rowKey="id"
            size="small"
            pagination={false}
            scroll={{ x: 1000 }}
          />
        </Card>
      )}
      <Card title="所有提醒">
        <Table
          columns={allColumns}
          dataSource={reminders}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1000 }}
        />
      </Card>
    </div>
  );
};

export default RemindersPage;
