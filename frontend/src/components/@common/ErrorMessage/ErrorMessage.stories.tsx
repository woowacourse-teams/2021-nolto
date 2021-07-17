import React from 'react';
import { useForm } from 'react-hook-form';
import FormInput from '../FormInput/FormInput';
import Label from '../Label/Label';

import ErrorMessage from './ErrorMessage';

export default {
  title: 'components/common/ErrorMessage',
  component: ErrorMessage,
  argTypes: {},
};

export const Progress = () => {
  const {
    register,
    setError,
    formState: { errors },
  } = useForm<{ title: string }>();

  setError('title', {
    type: 'required',
    message: '😭 프로젝트 이름을 알려주세요!',
  });

  return (
    <div>
      <Label text="제목" required={true} />
      <FormInput
        {...register('title', {
          required: '😭 프로젝트 이름을 알려주세요!',
        })}
      />
      <ErrorMessage targetError={errors.title} />
    </div>
  );
};
