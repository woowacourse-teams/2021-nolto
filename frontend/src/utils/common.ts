export const except = (object: any, keys: string[]) => {
  if (keys.some((key) => object?.[key] === undefined)) {
    console.error('except(object,keys) : 해당 key 값이 object에 존재하지 않습니다');
    return object;
  }

  const newObject = { ...object };

  keys.forEach((key) => {
    delete newObject[key];
  });

  return newObject;
};
