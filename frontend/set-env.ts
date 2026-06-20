import { readFileSync, writeFileSync } from 'fs';

// Read templates
const envTemplate = readFileSync('./src/environments/environment.template.ts', 'utf-8');

// Replace placeholders with actual env vars
const envContent = envTemplate
  .replace('${API_URL}', process.env.API_URL || 'http://localhost:8080/api')
  .replace('${GOOGLE_CLIENT_ID}', process.env.GOOGLE_CLIENT_ID || '');

// Write actual environment files (not committed)
writeFileSync('./src/environments/environment.ts', envContent);

console.log('✅ Environment files generated successfully!');
