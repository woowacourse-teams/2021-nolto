interface SVGProps {
  fill?: string;
  width: string;
}

declare module '*.svg' {
  const content: ({ fill, width }: SVGProps) => JSX.Element;

  export default content;
}
