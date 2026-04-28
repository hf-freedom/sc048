import React, { useState, useEffect } from 'react';
import { Table, Card, message } from 'antd';
import axios from 'axios';

const DoctorsPage = () => {
  const [doctors, setDoctors] = useState([]);
  const [schedules, setSchedules] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [doctorsRes, schedulesRes] = await Promise.all([
        axios.get('/api/doctors'),
        axios.get('/api/doctors/schedules/available', {
          params: {
            startDate: new Date().toISOString().split('T')[0],
            endDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000).toISOString().split('T')[0]
          }
        })
      ]);
      setDoctors(doctorsRes.data.data || []);
      setSchedules(schedulesRes.data.data || []);
    } catch (error) {
      message.error('加载数据失败');
    } finally {
      setLoading(false);
    }
  };

  const doctorColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '姓名', dataIndex: 'name', key: 'name', width: 100 },
    { title: '职称', dataIndex: 'title', key: 'title', width: 100 },
    { title: '科室', dataIndex: 'departmentName', key: 'departmentName', width: 100 },
    { title: '电话', dataIndex: 'phone', key: 'phone', width: 130 },
    { title: '状态', dataIndex: 'status', key: 'status', width: 80 }
  ];

  const scheduleColumns = [
    { title: '日期', dataIndex: 'date', key: 'date', width: 120 },
    { title: '医生', dataIndex: 'doctorName', key: 'doctorName', width: 100 },
    { title: '科室', dataIndex: 'departmentName', key: 'departmentName', width: 100 },
    { title: '开始时间', dataIndex: 'startTime', key: 'startTime', width: 100 },
    { title: '结束时间', dataIndex: 'endTime', key: 'endTime', width: 100 },
    { title: '最大接诊', dataIndex: 'maxPatients', key: 'maxPatients', width: 80 },
    { title: '已预约', dataIndex: 'bookedPatients', key: 'bookedPatients', width: 80 }
  ];

  return (
    <div>
      <Card title="医生列表" style={{ marginBottom: 24 }}>
        <Table
          columns={doctorColumns}
          dataSource={doctors}
          rowKey="id"
          loading={loading}
          pagination={false}
          size="small"
        />
      </Card>
      <Card title="可预约排班（未来7天）">
        <Table
          columns={scheduleColumns}
          dataSource={schedules}
          rowKey="id"
          loading={loading}
          size="small"
        />
      </Card>
    </div>
  );
};

export default DoctorsPage;
