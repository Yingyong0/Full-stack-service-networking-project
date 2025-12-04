from http.server import BaseHTTPRequestHandler, HTTPServer

class MyHttpHandler(BaseHTTPRequestHandler):
    def print_http_request_detail(self):
        """Print HTTP request in detail."""
        print('::Client address   : {0}'.format(self.client_address[0]))
        print('::Client port      : {0}'.format(self.client_address[1]))
        print('::Request command  : {0}'.format(self.command))
        print('::Request line     : {0}'.format(self.requestline))
        print('::Request path     : {0}'.format(self.path))
        print('::Request version  : {0}'.format(self.request_version))

    def send_http_response_header(self):
        """Create and send http Response."""
        self.send_response(200)
        self.send_header("Content-type", "text/html")
        self.end_headers()

    def do_GET(self):
        """HTTP GET request handler."""
        print("## do_GET() activated.")

        # GET response header generation
        self.print_http_request_detail()
        self.send_http_response_header()

        # GET request for multiplication (parameter transfer through Get request)
        # ex: http://localhost:8080/?var1=9&var2=9
        if '?' in self.path:
            # parameter retrieval and calculation
            routine = self.path.split('?')[1]
            parameter = self.parameter_retrieval(routine)
            result = self.simple_calc(int(parameter[0]), int(parameter[1]))

            # GET response generation
            self.wfile.write(bytes("<html>", "utf-8"))
            get_response = "GET request for calculation => {0} x {1} = {2}".format(parameter[0], parameter[1], result)
            self.wfile.write(bytes(get_response, "utf-8")) 
            self.wfile.write(bytes("</html>", "utf-8"))
            print("## GET request for calculation => {0} x {1} = {2}.".format(parameter[0], parameter[1], result))
        # GET request for directory retrieval
        # ex: http://localhost:8080/directory_sub/
        else:
            # GET response generation
            self.wfile.write(bytes("<html>", "utf-8"))
            self.wfile.write(bytes("<p>HTTP Request GET for Path: %s</p>" % self.path, "utf-8"))
            self.wfile.write(bytes("</html>", "utf-8"))
            print("## GET request for directory => {0}.".format(self.path))

    def do_POST(self):
        """HTTP POST request handler."""
        print("## do_POST() activated.")

        # GET response header generation
        self.print_http_request_detail()
        self.send_http_response_header()

        # parameter retrieval and calculation
        content_length = int(self.headers['Content-Length']) # <--- Gets the size of data
        post_data = self.rfile.read(content_length) # <--- Gets the data itself
        parameter = self.parameter_retrieval(post_data.decode('utf-8'))
        result = self.simple_calc(int(parameter[0]), int(parameter[1]))
        
        # POST response generation
        post_response = "POST request for calculation => {0} x {1} = {2}".format(parameter[0], parameter[1], result)
        self.wfile.write(bytes(post_response, "utf-8")) 
        print("## POST request data => {0}.".format(post_data.decode('utf-8')))   
        print("## POST request for calculation => {0} x {1} = {2}.".format(parameter[0], parameter[1], result))   

    def log_message(self, format, *args):
        """Turn off default http.server log message."""
        return

    def simple_calc(self, para1, para2):
        """Multiplication function."""
        return para1 * para2

    def parameter_retrieval(self, msg):
        """Parameter retrieval function for multiplication."""
        result = []
        fields = msg.split('&')
        result.append(int(fields[0].split('=')[1]))
        result.append(int(fields[1].split('=')[1]))
        return result

if __name__ == "__main__": 
    """Main function."""
    server_name = "localhost"
    server_port = 8080

    webServer = HTTPServer((server_name, server_port), MyHttpHandler)
    print("## HTTP server started at http://{0}:{1}.".format(server_name, server_port))

    try:
        webServer.serve_forever()
    except KeyboardInterrupt:
        pass

    webServer.server_close()
    print("HTTP server stopped.")