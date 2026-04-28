import React, { useState, useEffect } from 'react';
import { Table, Card, Tag, message } from 'antd';
import axios from 'axios';

const MedicinesPage = () => {
  const [medicines, setMedicines] = useState([]);
  const [batches, setBatches] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    try {
      const [medsRes, batchesRes] = await Promise.all([
        axios.get('/api/medicines'),
        axios.get('/api/medicines/batches/expiring', { params: { daysThreshold: 30 } })
      ]);
      setMedicines(medsRes.data.data || []);
      setBatches(batchesRes.data.data || []);
    } catch (error) {
      message.error('加载药品数据失败');
    } finally {
      setLoading(false);
    }
  };

  const medicineColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id', width: 60 },
    { title: '编码', dataIndex: 'code', key: 'code', width: 100 },
    { title: '名称', dataIndex: 'name', key: 'name', width: 120 },
    { title: '规格', dataIndex: 'specification', key: 'specification', width: 120 },
    { title: '单位', dataIndex: 'unit', key: 'unit', width: 80 },
    { title: '售价', dataIndex: 'salePrice', key: 'salePrice', width: 100 },
    { title: '成本', dataIndex: 'costPrice', key: 'costPrice', width: 100 },
    { title: '分类', dataIndex: 'category', key: 'category', width: 100 },
    { title: '状态', dataIndex: 'status', key: 'status', width: 80 }
  ];

  const batchColumns = [
    { title: '药品名称', dataIndex: 'medicineName', key: 'medicineName', width: 120 },
    { title: '批次号', dataIndex: 'batchNumber', key: 'batchNumber', width: 120 },
    { title: '生产日期', dataIndex: 'productionDate', key: 'productionDate', width: 120 },
    { title: '有效期', dataIndex: 'expiryDate', key: 'expiryDate', width: 120,
      render: (date) => <Tag color="red">{date}</Tag>
    },
    { title: '总数量', dataIndex: 'quantity', key: 'quantity', width: 80 },
    { title: '可用数量', dataIndex: 'availableQuantity', key: 'availableQuantity', width: 100 },
    { title: '供应商', dataIndex: 'supplier', key: 'supplier', width: 150 }
  ];

  return (
    <div>
      <Card title="药品列表" style={{ marginBottom: 24 }}>
        <Table
          columns={medicineColumns}
          dataSource={medicines}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1000 }}
          size="small"
        />
      </Card>
      <Card title="即将过期的药品批次（30天内）">
        <Table
          columns={batchColumns}
          dataSource={batches}
          rowKey="id"
          loading={loading}
          scroll={{ x: 1000 }}
          size="small"
          pagination={false}
        />
        {batches.length === 0 && <div style={{ textAlign: 'center', color: '#52c41a' }}>暂无即将过期的药品</div>}
      </Card>
    </div>
  );
};

export default MedicinesPage;
