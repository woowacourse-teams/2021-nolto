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

const isIso8601 = (date: string) => {
  return /[0-9]+-[0-9]+-[0-9]+T.+/.test(date);
};

export const refineDate = (date: string) => {
  if (!isIso8601(date)) {
    console.error('refineDate() : 잘못된 date 타입입니다');
    return date;
  }

  return date
    .replace(/T.+$/, '')
    .replace(/-/g, '/')
    .replace(/[0-9]+\//, '');
};
