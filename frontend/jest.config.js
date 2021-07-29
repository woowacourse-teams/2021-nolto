const { pathsToModuleNameMapper } = require('ts-jest/utils');

const { compilerOptions } = require('./tsconfig');

module.exports = {
  preset: 'ts-jest',
  setupFilesAfterEnv: ['./src/setUpTests.ts'],
  coveragePathIgnorePatterns: ['./node_modules/'],
  testEnvironment: 'jsdom',
  globals: {
    'ts-jest': {
      isolatedModules: true,
    },
  },
  moduleDirectories: ['node_modules'],
  // moduleNameMapper: {
  //   '^@src(.*)$': './src/$1',
  // },
  moduleNameMapper: pathsToModuleNameMapper(compilerOptions.paths /*, { prefix: '<rootDir>/' } */),
};
