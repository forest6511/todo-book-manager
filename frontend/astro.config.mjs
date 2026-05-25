import { defineConfig } from 'astro/config';

export default defineConfig({
  // Astro のビルド成果物を Spring Boot の static/ にコピーする
  // Docker ビルド時に自動的にコピーされる
  output: 'static',
});
