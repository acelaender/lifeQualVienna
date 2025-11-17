# Build Angular app
FROM node:20 AS build
WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build --prod

# publish on nginx
FROM nginx:stable
COPY --from=build /app/dist/ui/browser /usr/share/nginx/html

EXPOSE 80
