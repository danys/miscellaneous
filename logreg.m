1;

% Logistic Regression implementation

% Generate a matrix with random decimals. Size of the matrix, 
% min and max value of the decimal elements adjustable
function pv = generateVector(maxx, minx, dimx, dimy)
  pv = minx+(maxx-minx)*rand(dimx, dimy);
end

% Given two points on a line as linePoints assign xPoints to +1
% if it is above the line and to -1 if it is below the line
function y = evalY(linePoints, xPoints)
  [lenx, leny] = size(xPoints);
  y = ones(lenx, 1);
  a = (linePoints(2,2)-linePoints(1,2))/(linePoints(2,1)-linePoints(1,1));
  b = linePoints(1,2)-a*linePoints(1,1);
  for i = 1:lenx
    y(i,1) = sign(xPoints(i,2)-a*xPoints(i,1)-b);
  end
end

% Compute the cross-entropy error
function err = logError(y, w, xPoints)
  [xlen ylen] = size(xPoints);
  err = (1/xlen)*sum(log(1.+exp(-1.*y'.*(w'*xPoints'))));
end

% Permute the rows of the x matrix and the y vector
function [x, y] = permuteXandY(x,y)
  [xlen, ylen] = size(x);
  p = randperm(xlen);
  for i=1:xlen
    index = p(1,i);
    tx = x(i,:);
    x(i,:) = x(index,:);
    x(index,:) = tx;
    ty = y(i,:);
    y(i,:) = y(index, :);
    y(index, :) = ty;
  end
end

% Compute the gradient of the error function at some point xPoint
function grad = derivative(xPoint, yVal, w)
  grad = (-1.*yVal.*xPoint)./(1+exp(yVal*w'*xPoint'));
end

% Script entry point
% 1. Choose a random line the plane
% 2. Generate N random points
% 3. Assign +1 to point above the line and -1 to those below
% 4. Use stochastic gradient descent with logistic regression
%    to find a good fit for the data
% 5. Compute the out of same error
% This procedure is repeated nIterations time and the different 
% measures are averaged.
more off;
maxx = 1;
minx = -1;
N = 100;
nIterations = 100;
minProgress = 0.01;
n = 0.01;
minError = 10e-1;
dimensions = 2;
eout = zeros(nIterations, 1);
epochs = zeros(nIterations, 1);
for i = 1:nIterations
  linePoints = generateVector(maxx, minx, 2, dimensions);
  xPoints = generateVector(maxx, minx, N, dimensions);
  x = [ones(N,1) xPoints(:,1) xPoints(:,2)];
  y = evalY(linePoints, xPoints);
  w = zeros(dimensions+1,1);
  fprintf("Iteration %i\n",i);
  deltaw = minProgress;
  nEpochs = 0;
  while deltaw >= minProgress
    [x y] = permuteXandY(x, y);
    initw = w;
    for j = 1:N
      err = minError+1;
      deltaw2 = minProgress;
      while deltaw2 >= minProgress
        initw2 = w;
        g = derivative(x(j,:),y(j,:),w);
        ng = n.*g;
        w = w - ng';
        deltaw2 = sqrt((w-initw2)'*(w-initw2));
      end
    end
    deltaw = sqrt((w-initw)'*(w-initw));
    nEpochs = nEpochs + 1;
  end
  newPoints = generateVector(maxx, minx, N, dimensions);
  newX = [ones(N,1) newPoints(:,1) newPoints(:,2)];
  newY = evalY(linePoints, newPoints);
  eout(i, 1) = logError(newY, w, newX);
  epochs(i, 1) = nEpochs;
  fprintf("w = [%.4f %.4f %.4f]\n", w(1,1), w(2,1), w(3,1));
  fprintf("Number of epochs = %i\n", nEpochs);
  fprintf("Eout = %.4f\n\n", eout(i, 1));
end
avgEout = (1/nIterations)*sum(eout);
avgEpochs = (1/nIterations)*sum(epochs);
printf("Avg Eout: %.4f\n", avgEout);
printf("Avg number of epochs: %.4f\n", avgEpochs);