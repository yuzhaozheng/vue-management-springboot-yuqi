<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>
          <i class="el-icon-lx-cascades"></i> 职位管理
        </el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="container">
      <div class="handle-box">
        <el-button type="primary" icon="el-icon-delete" class="handle-del mr10" @click="delAllSelection">批量删除</el-button>
        <el-input v-model="query.positionId" placeholder="职位ID" class="handle-input mr10" style="width:120px;" />
        <el-input v-model="query.name" placeholder="名称" class="handle-input mr10" style="width:120px;" />
        <el-input v-model="query.code" placeholder="编码" class="handle-input mr10" style="width:120px;" />
        <el-input v-model="query.createTime" placeholder="创建时间" class="handle-input mr10" style="width:120px;" />
        <el-input v-model="query.updateTime" placeholder="修改时间" class="handle-input mr10" style="width:120px;" />
        <el-input v-model="query.status" placeholder="状态(0启用, 1停用)" class="handle-input mr10" style="width:120px;" />
        <el-input v-model="query.del" placeholder="删除(0正常, 1删除)" class="handle-input mr10" style="width:120px;" />
        <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
        <el-button type="primary" icon="el-icon-circle-plus-outline" v-if="hasPermission('/position/add')" @click="handleAdd">添加</el-button>
      </div>
      <el-table :data="tableData" border class="table" ref="multipleTable" header-cell-class-name="table-header" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="positionId" label="职位ID" width="55" align="center" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="code" label="编码" />
        <el-table-column prop="createTime" label="创建时间" />
        <el-table-column prop="updateTime" label="修改时间" />
        <el-table-column prop="status" label="状态(0启用, 1停用)" />
        <el-table-column prop="del" label="删除(0正常, 1删除)" />
        <el-table-column label="操作" width="180" align="center" v-if="hasPermission('/position/edit', '/position/delete')">
          <template slot-scope="scope">
            <el-button type="text" icon="el-icon-edit" v-if="hasPermission('/position/edit')" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button type="text" icon="el-icon-delete" v-if="hasPermission('/position/delete')" class="red" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination">
        <el-pagination @size-change="handlePageChange" @current-change="handleCurrentChange" :current-page="query.current" :total="total" :page-sizes="[5, 10, 15, 20]" :page-size="query.size" layout="total, sizes, prev, pager, next, jumper" />
      </div>
    </div>

    <!-- 添加弹出框 -->
    <el-dialog title="添加" v-dialogDrag :visible.sync="addVisible" width="30%">
      <el-form ref="form" :model="form" label-width="70px">
        <el-form-item label="名称" label-width="70px">
          <el-input v-model="form.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="编码" label-width="70px">
          <el-input v-model="form.code" autocomplete="off" />
        </el-form-item>
        <el-form-item label="创建时间" label-width="70px">
          <el-input v-model="form.createTime" autocomplete="off" />
        </el-form-item>
        <el-form-item label="修改时间" label-width="70px">
          <el-input v-model="form.updateTime" autocomplete="off" />
        </el-form-item>
        <el-form-item label="状态(0启用, 1停用)" label-width="70px">
          <el-input v-model="form.status" autocomplete="off" />
        </el-form-item>
        <el-form-item label="删除(0正常, 1删除)" label-width="70px">
          <el-input v-model="form.del" autocomplete="off" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveAdd">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 编辑弹出框 -->
    <el-dialog title="编辑" v-dialogDrag :visible.sync="editVisible" width="30%">
      <el-form ref="form" :model="form" label-width="70px">
            <el-form-item label="名称" label-width="70px">
              <el-input v-model="form.name" autocomplete="off" />
            </el-form-item>
            <el-form-item label="编码" label-width="70px">
              <el-input v-model="form.code" autocomplete="off" />
            </el-form-item>
            <el-form-item label="创建时间" label-width="70px">
              <el-input v-model="form.createTime" autocomplete="off" />
            </el-form-item>
            <el-form-item label="修改时间" label-width="70px">
              <el-input v-model="form.updateTime" autocomplete="off" />
            </el-form-item>
            <el-form-item label="状态(0启用, 1停用)" label-width="70px">
              <el-input v-model="form.status" autocomplete="off" />
            </el-form-item>
            <el-form-item label="删除(0正常, 1删除)" label-width="70px">
              <el-input v-model="form.del" autocomplete="off" />
            </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveEdit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { fetchPosition, savePosition, updatePosition, deletePosition } from '../../api/position';
export default {
    name: 'basetable',
    data() {
        return {
            query: {
                current: 1,
                size: 10
            },
            tableData: [],
            multipleSelection: [],
            delList: [],
            addVisible: false,
            editVisible: false,
            total: 0,
            form: {},
            idx: -1,
            id: -1
        };
    },
    created() {
        this.getData();
    },
    methods: {
        // 获取分页数据
        getData() {
            fetchPosition(this.query).then(res => {
                this.tableData = res.records;
                this.total = res.total;
            });
        },
        // 触发搜索按钮
        handleSearch() {
            this.getData();
        },
        // 删除操作
        handleDelete(index, row) {
            // 二次确认删除
            this.$confirm('确定要删除吗？', '提示', {
                type: 'warning'
            })
                .then(() => {
                    deletePosition({ id: row.positionId }).then(res => {
                        this.getData();
                        this.$message.success('删除成功');
                    });
                })
                .catch(() => {});
        },
        // 多选操作
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        delAllSelection() {
            const length = this.multipleSelection.length;
            let ids = [];
            this.multipleSelection.forEach(row => {
                ids.push(row.positionId);
            });
            deletePosition({ id: ids }).then(res => {
                this.getData();
                this.multipleSelection = [];
                this.$message.success('删除成功');
            });
        },
        // 添加操作
        handleAdd(index, row) {
            this.form = {};
            this.addVisible = true;
        },
        // 保存添加
        saveAdd() {
            savePosition(this.form).then(res => {
                this.getData();
                this.addVisible = false;
                this.$message.success('保存成功');
            });
        },
        // 编辑操作
        handleEdit(index, row) {
            this.idx = index;
            this.form = Object.assign({}, row);
            this.editVisible = true;
        },
        // 保存编辑
        saveEdit() {
            updatePosition(this.form).then(res => {
                this.getData();
                this.editVisible = false;
                this.$message.success('修改成功');
            });
        },
        // 分页导航
        handleCurrentChange(val) {
            this.query.current = val;
            this.getData();
        },
        handlePageChange(val) {
            this.query.size = val;
            this.getData();
        }
    }
};
</script>

<style scoped>
.handle-box {
    margin-bottom: 20px;
}

.handle-select {
    width: 120px;
}

.handle-input {
    width: 300px;
    display: inline-block;
}
.table {
    width: 100%;
    font-size: 14px;
}
.red {
    color: #ff0000;
}
.mr10 {
    margin-right: 10px;
}
.table-td-thumb {
    display: block;
    margin: auto;
    width: 40px;
    height: 40px;
}
</style>
