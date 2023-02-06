# Define node version
FROM node:18-alpine AS builder
# Define container directory
WORKDIR /usr/src/app
# Copy package*.json for npm install
COPY package.json .
# Run npm install @angular/cli
RUN npm install --save-dev @angular/cli@latest
# Install all the dependencies
RUN npm install
# Copy all files
COPY . /usr/src/app
RUN ls
# Run ng build through npm to create dist folder
RUN npm run build --prod
# #Run the project
# CMD ["npm","start"] 
RUN echo "cameeeee11"
# Use official nginx image as the base image
FROM nginx:1.17.1-alpine
RUN echo "cameeeee"
COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=builder /usr/src/app/dist/sun-Travel  /usr/share/nginx/html/
