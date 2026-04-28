import React, { useState, useEffect } from 'react';
import { Card, Row, Col, Statistic, Table, Tag } from 'antd';
import {
  MedicineBoxOutlined,
  UserOutlined,
  ScheduleOutlined,
  BellOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined
} from '@ant-design/icons';
import axios from 'axios';

const HomePage = () => {
  const [stats, setStats] = useState({
    pets: 0,
    owners: 0,
    doctors: 0,
    reminders: 0,
    pendingConsultations: 0,
    pendingBoardings: 0,
    pendingBills: 0
  });

  const [recentConsultations, setRecentConsultations] = useState([]);
  const [pendingReminders, setPendingReminders] = useState([]);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      const [petsRes, ownersRes, doctorsRes, consultationsRes, remindersRes] = await Promise.all([
        axios.get('/api/pets'),
        axios.get('/api/owners'),
        axios.get('/api/doctors'),
        axios.get('/api/consultations'),
        axios.get('/api/reminders/pending')
      ]);

      setStats(prev => ({
        ...prev,
        pets: petsRes.data.data?.length || 0,
        owners: ownersRes.data.data?.length || 0,
        doctors: doctorsRes.data.data?.length || 0,
        pendingConsultations: consultationsRes.data.data?.filter(c => c.status === 'IN_PROGRESS').length || 0,
        reminders: remindersRes.data.data?.length || 0
      }));

      setRecentConsultations(consultationsRes.data.data?.slice(-5) || []);
      setPendingReminders(remindersRes.data.data?.slice(0, 5) || []);
    } catch (error) {
      console.error('Failed to load data:', error);
    }
  };

  const consultationColumns = [
    { title: '编号', dataIndex: 'code', key: 'code' },
    { title: '宠物', dataIndex: 'petName', key: 'petName' },
    { title: '医生', dataIndex: 'doctorName', key: 'doctorName' },
    { 
      title: '状态', 
      dataIndex: 'status', 
      key: 'status',
      render: (status) => {
        const colors = {
          'PENDING': 'gold',
          'IN_PROGRESS': 'blue',
          'COMPLETED': 'green',
          'CANCELLED': 'red'
        };
        return <Tag color={colors[status] || 'default'}>{status}</Tag>;
      }
    }
  ];

  const reminderColumns = [
    { title: '类型', dataIndex: 'type', key: 'type' },
    { title: '宠物', dataIndex: 'petName', key: 'petName' },
    { title: '内容', dataIndex: 'content', key: 'content' },
    { title: '提醒日期', dataIndex: 'remindDate', key: 'remindDate' }
  ];

  return (
    <div>
      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={6}>
          <Card className="stats-card">
            <Statistic
              title="宠物总数"
              value={stats.pets}
              prefix={<MedicineBoxOutlined />}
              valueStyle={{ color: '#3f8600' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card className="stats-card">
            <Statistic
              title="主人总数"
              value={stats.owners}
              prefix={<UserOutlined />}
              valueStyle={{ color: '#1890ff' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card className="stats-card">
            <Statistic
              title="医生数量"
              value={stats.doctors}
              prefix={<ScheduleOutlined />}
              valueStyle={{ color: '#722ed1' }}
            />
          </Card>
        </Col>
        <Col span={6}>
          <Card className="stats-card">
            <Statistic
              title="待处理提醒"
              value={stats.reminders}
              prefix={<BellOutlined />}
              valueStyle={{ color: '#cf1322' }}
            />
          </Card>
        </Col>
      </Row>

      <Row gutter={16}>
        <Col span={12}>
          <Card title="最近就诊记录">
            <Table
              columns={consultationColumns}
              dataSource={recentConsultations}
              rowKey="id"
              pagination={false}
              size="small"
            />
          </Card>
        </Col>
        <Col span={12}>
          <Card title="待处理提醒">
            <Table
              columns={reminderColumns}
              dataSource={pendingReminders}
              rowKey="id"
              pagination={false}
              size="small"
            />
          </Card>
        </Col>
      </Row>
    </div>
  );
};

export default HomePage;
