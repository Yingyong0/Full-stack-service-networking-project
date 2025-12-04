from flask import Flask, request
from flask_restx import Resource, Api

app = Flask(__name__)

# 根路径 - 显示 API 信息页面（必须在 Api 初始化之前注册）
@app.route('/')
def index():
    return '''
    <!DOCTYPE html>
    <html>
        <head>
            <title>Membership REST API</title>
            <style>
                body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }
                .container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
                h1 { color: #333; }
                h2 { color: #666; margin-top: 30px; }
                code { background: #f4f4f4; padding: 2px 6px; border-radius: 3px; }
                .endpoint { background: #f9f9f9; padding: 15px; margin: 10px 0; border-left: 4px solid #007bff; }
                .method { display: inline-block; padding: 4px 8px; border-radius: 3px; font-weight: bold; margin-right: 10px; }
                .get { background: #28a745; color: white; }
                .post { background: #007bff; color: white; }
                .put { background: #ffc107; color: black; }
                .delete { background: #dc3545; color: white; }
                a { color: #007bff; text-decoration: none; }
                a:hover { text-decoration: underline; }
            </style>
        </head>
        <body>
            <div class="container">
                <h1>🏠 Membership REST API</h1>
                <p>欢迎使用会员管理 REST API 服务</p>
                
                <h2>📚 API 文档</h2>
                <p>访问 <a href="/api" target="_blank">Swagger UI 文档</a> 查看完整的 API 文档和交互式测试界面</p>
                
                <h2>🔗 API 端点</h2>
                <p>基础 URL: <code>http://127.0.0.1:5000/membership_api/&lt;member_id&gt;</code></p>
                
                <div class="endpoint">
                    <span class="method get">GET</span>
                    <strong>/membership_api/&lt;member_id&gt;</strong>
                    <p>读取会员信息</p>
                    <p>示例: <code>GET /membership_api/0001</code></p>
                </div>
                
                <div class="endpoint">
                    <span class="method post">POST</span>
                    <strong>/membership_api/&lt;member_id&gt;</strong>
                    <p>创建新会员</p>
                    <p>示例: <code>POST /membership_api/0001</code> (数据: <code>{'0001': 'apple'}</code>)</p>
                </div>
                
                <div class="endpoint">
                    <span class="method put">PUT</span>
                    <strong>/membership_api/&lt;member_id&gt;</strong>
                    <p>更新会员信息</p>
                    <p>示例: <code>PUT /membership_api/0001</code> (数据: <code>{'0001': 'orange'}</code>)</p>
                </div>
                
                <div class="endpoint">
                    <span class="method delete">DELETE</span>
                    <strong>/membership_api/&lt;member_id&gt;</strong>
                    <p>删除会员</p>
                    <p>示例: <code>DELETE /membership_api/0001</code></p>
                </div>
                
                <h2>🧪 测试 API</h2>
                <p>你可以通过以下方式测试 API：</p>
                <ul>
                    <li>使用 <a href="/api" target="_blank">Swagger UI</a> 进行交互式测试</li>
                    <li>运行客户端程序: <code>python lec-06-prg-08-rest-client-v3.py</code></li>
                    <li>使用浏览器直接访问: <a href="/membership_api/0001" target="_blank">/membership_api/0001</a></li>
                </ul>
                
                <h2>📝 响应格式</h2>
                <p>所有响应都是 JSON 格式：</p>
                <ul>
                    <li>成功: <code>{"member_id": "value"}</code></li>
                    <li>不存在: <code>{"member_id": "None"}</code></li>
                    <li>已删除: <code>{"member_id": "Removed"}</code></li>
                </ul>
            </div>
        </body>
    </html>
    '''

# 将 API 文档放在 /api 路径，避免与根路径冲突
api = Api(app, doc='/api', title='Membership API', description='REST API for membership management')

class MembershipHandler():
    # dictionary for membership management
    database = {} 

    # POST request
    def create(self, id, value):
        if id in self.database :
            return {id : "None"}
        else:
            self.database[id] = value
            return {id : self.database[id]}

    # GET request
    def read(self, id):
        if id in self.database:
            return {id : self.database[id]}
        else:
            return {id : "None"}

    # PUT request
    def update(self, id, value):
        if id in self.database :
            self.database[id] = value
            return {id : self.database[id]}
        else:
            return {id : "None"}

    # DELETE request
    def delete(self, id):
        if id in self.database :
            del self.database[id]
            return {id : "Removed"}
        else:
            return {id : "None"}

myManager = MembershipHandler()

@api.route('/membership_api/<string:member_id>')
class MembershipManager(Resource):
    # 'C'reate handler
    def post(self, member_id):
        return myManager.create(member_id, request.form[member_id])
    # 'R'emove handler
    def get(self, member_id):
        return myManager.read(member_id)
    # 'U'pdate handler
    def put(self, member_id):
        return myManager.update(member_id, request.form[member_id])
    # 'D'elete handler
    def delete(self, member_id):
        return myManager.delete(member_id)

if __name__ == '__main__':
    app.run(debug=True)
    