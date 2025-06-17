# Etapa 1: Construir la aplicación Angular SSR
FROM node:20-alpine AS builder

WORKDIR /app

# Copiar los archivos de dependencias
COPY package.json package-lock.json* ./
RUN npm install

# Copiar el resto del código
COPY . .

# Compilar Angular SSR (cliente y servidor)
RUN npm run build

# Etapa 2: Imagen final para ejecución con Node.js
FROM node:20-alpine AS runner

WORKDIR /app

ENV NODE_ENV=production

# Copiar los archivos compilados del builder
COPY --from=builder /app/dist /app/dist
COPY --from=builder /app/node_modules /app/node_modules
COPY --from=builder /app/package.json /app/package.json

EXPOSE 4000

# Cambia esto si el nombre del proyecto es diferente
CMD ["node", "dist/arena-xperience-front/server/server.mjs"]
