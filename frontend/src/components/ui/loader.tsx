import CircularProgress from "@mui/material/CircularProgress";
import Box from "@mui/material/Box";

interface LoaderProps {
  size?: number; 
  label?: string;
  className?:string
  className2?:string
}

const Loader = ({ size = 60, label,className,className2 }:LoaderProps) => {
  return (
    <Box className={`flex flex-col justify-center items-center flex-1 ${className2}` }>
      {label && (
        <div className={`mb-4 text-center text-dark2-gray  flex items-center ${className}` }>
          {label}
        </div>
      )}
      <CircularProgress
        className={`text-blue-600! ${className}`}
        size={size}
      />
    </Box>
  );
};

export default Loader;
