import { defineConfig, Plugin } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";
import { createServer } from "./server";

export default defineConfig(() => ({
  root: path.resolve(__dirname, "client"),
  server: {
    host: "::",
    port: 8080,
    proxy: {
      '/api/tasks': {
        target: 'http://localhost:8087',
        changeOrigin: true,
        secure: false,
      },
    },
    fs: {
      allow: [
        path.resolve(__dirname, "client"),
        path.resolve(__dirname, "shared"),
        path.resolve(__dirname, "node_modules"),
      ],
      strict: true,
    },
  },
  build: {
    outDir: "dist", // CHANGED: Output to dist for Vercel deployment
  },
  plugins: [react(), expressPlugin()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "client"),
      "@shared": path.resolve(__dirname, "shared"),
    },
  },
}));

function expressPlugin(): Plugin {
  return {
    name: "express-plugin",
    apply: "serve",
    configureServer(server) {
      const app = createServer();
      server.middlewares.use((req, res, next) => {
        if (req.url?.startsWith('/api/tasks')) {
          return next();
        }
        if (req.url?.startsWith('/api')) {
          return app(req, res, next);
        }
        next();
      });
    },
  };
}
