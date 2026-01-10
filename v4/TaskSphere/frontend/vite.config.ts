import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";

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
    outDir: "dist",
  },
  plugins: [react()], // REMOVED: expressPlugin() for static build
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "client"),
      "@shared": path.resolve(__dirname, "shared"),
    },
  },
}));
